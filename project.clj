(defproject slack-dice "0.1.0-SNAPSHOT"
  :description       "Example Slack app that rolls dice."
  :url               "https://github.com/bnadlerjr/slack-dice"
  :main              slack-dice.main
  :min-lein-version  "2.5.3"
  :source-paths      ["src"]
  :resource-paths    ["resources"]
  :test-paths        ["test"]

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :aliases {"doc" ["do" ["marg" "-f" "index.html"]]
            "lint" ["do" ["ancient"] ["kibit"] ["eastwood"]]
            "server" ["do" ["clean"] ["uberjar"] ["shell" "heroku" "local"]]}

  :dependencies [[clj-http                   "3.7.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [commons-validator          "1.6"]
                 [environ                    "1.1.0"]
                 [http-kit                   "2.2.0"]
                 [bnadlerjr/logfmt           "0.1.0"]
                 [metosin/compojure-api      "1.1.11"]
                 [org.clojure/clojure        "1.9.0"]
                 [org.clojure/core.async     "0.3.465"]
                 [org.clojure/data.json      "0.2.6"]
                 [ring/ring-devel            "1.6.3"]]

  :plugins [[jonase/eastwood "0.2.5"]
            [lein-ancient    "0.6.15"]
            [lein-environ    "1.1.0"]
            [lein-kibit      "0.1.6"]
            [lein-shell      "0.5.0"]
            [lein-marginalia "0.9.1"]]

  :profiles {:dev-common {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                         [org.clojure/tools.nrepl     "0.2.13"]
                                         [pjstadig/humane-test-output "0.8.3"]
                                         [reloaded.repl               "0.2.4"]]
                          :injections [(require 'pjstadig.humane-test-output)
                                       (pjstadig.humane-test-output/activate!)]
                          :repl-options {:init-ns user}
                          :source-paths ["dev-src"]}
             :dev-overrides {}
             :dev [:dev-common :dev-overrides]
             :uberjar {:aot [:all]
                       :omit-source true
                       :uberjar-name "slack-dice.jar"}})
