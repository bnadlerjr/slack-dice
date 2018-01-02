(ns slack-dice.components.env
  (:require [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [logfmt.core :refer [set-dev-mode!]]))

(defrecord Env []
  component/Lifecycle
  (start [this]
    (when (= "development" (env :ring-env)) (set-dev-mode! true))
    (assoc this :port (Integer. (env :port))
                :slack-verification-token (env :slack-verification-token)))
  (stop [this]
    (set-dev-mode! false)
    (assoc this :port nil
                :slack-verification-token nil)))

(defn create-env []
  (Env.))
