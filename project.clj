(defproject flickr-fetcher "0.0.1-SNAPSHOT"
  :description "Download images from flicker feed."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]
                 [clj-http "3.10.0"]
                 [cheshire "5.9.0"]
                 [funcool/cuerdas "2.2.0"]
                 [net.mikera/imagez "0.12.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "flickr-fetcher.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.7"]]}
             :uberjar {:aot [flickr-fetcher.server]}}
  :plugins [[lein-nsorg "0.3.0"]]
  :main ^{:skip-aot true} flickr-fetcher.server)
