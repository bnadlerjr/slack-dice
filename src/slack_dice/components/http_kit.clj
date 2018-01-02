(ns slack-dice.components.http-kit
  (:require [org.httpkit.server :refer [run-server]]
            [com.stuartsierra.component :as component]
            [logfmt.core :as logging]))

(defrecord Http-kit [env handler]
  component/Lifecycle
  (start [component]
    (let [port (:port env)]
      (logging/info (str "Starting http-kit server on port " port))
      (assoc component :http-kit (run-server (:handler handler) {:port port}))))
  (stop [{:keys [http-kit] :as component}]
    (logging/info "Stopping http-kit server")
    (when http-kit (http-kit))
    (assoc component :http-kit nil)))

(defn start-http-kit []
  (Http-kit. nil nil))
