(ns slack-dice.commands
  (:require [clj-http.client :as client]
            [clojure.core.async :as async]
            [clojure.data.json :as json]
            [clojure.string :refer [blank? split]]
            [logfmt.core :as log]))

(defn post-to-slack
  [url message]
  (let [{:keys [status reason-phrase] :as response}
        (client/post url {:body (json/write-str message)
                          :content-type :json})]
    (log/info
      (format "Completed POST '%s' %s %s" url status reason-phrase)
      {:url url :status status :payload message})))

(defn roll
  [num-die faces response_url]
  (let [result (reduce + (repeatedly num-die #(inc (rand-int faces))))]
    (post-to-slack
      response_url
      {:attachments
       [{:text (format "Rolled %s %s-sided die and got %s" num-die faces result)
         :color "good"}]})))

(defn parse-roll-action
  [text]
  (cond
    (or (= "help" text)
        (blank? text)) :help
    (re-find #"\d+d\d+" text) :valid-dice))

(defn process-roll
  [{:keys [text response_url]}]
  (let [help-message "Type `/roll AdX` where A is the number of die to roll and X is the number of faces." 
        action (parse-roll-action text)]
    (condp = action
      :help {:attachments [{:text help-message}]}
      :valid-dice (let [[die faces] (map #(Integer. %) (split text #"d"))]
                    (async/thread (roll die faces response_url))
                    {:attachments [{:text "Rolling some dice..."}]})
      {:attachments [{:text (str "Sorry, I didn't understand that command. " help-message)
                      :color "danger"}]})))
