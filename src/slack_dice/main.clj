(ns slack-dice.main
  (:gen-class)
  (:require [com.stuartsierra.component :as component :refer [using]]
            [slack-dice.components.http-kit :refer [start-http-kit]]
            [slack-dice.components.env :refer [create-env]]
            [slack-dice.components.handler :refer [create-handler]]))

(defn base-system []
  (component/system-map
    :env           (create-env)
    :handler       (using (create-handler 'slack-dice.api/slack-dice-api) [:env])
    :http-server   (using (start-http-kit) [:handler :env])))

(defn -main [& _]
  (component/start (base-system)))
