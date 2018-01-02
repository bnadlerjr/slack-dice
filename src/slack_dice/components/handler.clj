(ns slack-dice.components.handler
  (:require [com.stuartsierra.component :as component]
            [compojure.api.middleware :refer [wrap-components]]
            [logfmt.ring.middleware :refer [wrap-logger]]
            [ring.util.http-response :as respond-with]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]))

(defn wrap-token-verification
  [handler token]
  (fn [request]
    (if (= token (get-in request [:params :token] "unknown"))
      (handler request)
      (respond-with/unauthorized "Unable to verify request token."))))

(defn wrap-handler
  [handler {:keys [env] :as components}]
  (-> handler
      (wrap-token-verification (:slack-verification-token env))
      (wrap-components components)
      wrap-logger
      wrap-keyword-params
      wrap-params))

(defrecord Handler [sym]
  component/Lifecycle
  (start [this]
    (require (symbol (namespace sym)))
    (assoc this :handler (wrap-handler (resolve sym) this)))
  (stop [this]
    this))

(defn create-handler [sym]
  (Handler. sym))
