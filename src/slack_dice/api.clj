(ns slack-dice.api
  (:require [compojure.api.exception :as ex]
            [compojure.api.sweet :refer :all]
            [logfmt.core :as log]
            [ring.util.http-response :refer :all])
  (:require [slack-dice.commands :as commands]
            [slack-dice.schemas :as schemas]))

(defn wrap-logging
  [f]
  (fn [^Exception e data request]
    (log/error (.getMessage e))
    (f e data request)))

(defapi slack-dice-api
  {:format {:formats [:json]}
   :exceptions
   {:handlers
    {::ex/request-validation  (wrap-logging ex/request-validation-handler)
     ::ex/request-parsing     (wrap-logging ex/request-parsing-handler)
     ::ex/response-validation (wrap-logging ex/response-validation-handler)
     ::ex/default             (wrap-logging ex/safe-handler)}}}

  (context "/commands" []
           (POST "/roll" [& params]
                 :return schemas/Message
                 (ok (commands/process-roll params)))))
