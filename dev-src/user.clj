(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [slack-dice.main :refer [base-system]]))

(reloaded.repl/set-init! base-system)
