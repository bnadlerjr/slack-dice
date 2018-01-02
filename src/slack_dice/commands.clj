(ns slack-dice.commands
  (:require [clj-http.client :as client]
            [clojure.core.async :as async]
            [clojure.data.json :as json]
            [logfmt.core :as log]))

(defn post-to-slack
  [url message]
  (let [{:keys [status reason-phrase] :as response}
        (client/post url {:body (json/write-str message)
                          :content-type :json})]
    (log/info
      (format "Completed POST '%s' %s %s" url status reason-phrase)
      {:url url :status status :payload message})))

(def ping-help-message
  {:attachments [{:text "Type `/ping` to ping the server."}]})

(defn ping
  [env response_url]
  (post-to-slack response_url
                 {:attachments [{:text "Ping successful."
                                 :color "good"
                                 :fields [{:title "Port"
                                           :value (:port env)}]}]}))

(defn process-ping
  [env {:keys [text response_url]}]
  (cond
    (= "help" text) ping-help-message
    :else (do
            (async/thread (ping env response_url))
            {:attachments [{:text "Pinging server..."}]})))
