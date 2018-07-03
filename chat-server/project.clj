(defproject chat-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [clojure.java-time "0.3.2"]
                 [com.stuartsierra/dependency "0.2.0"]
                 [duct/core "0.6.2"]
                 [duct/database.sql.hikaricp "0.3.3"]
                 ;; [duct/module.cljs "0.3.2"]
                 [duct/module.logging "0.3.1"]
                 [duct/module.sql "0.4.2"]
                 ;; [duct/module.web "0.6.4"]
                 [honeysql "0.9.3"]
                 [integrant "0.7.0-alpha2"]
                 [io.pedestal/pedestal.jetty "0.5.4"]
                 [io.pedestal/pedestal.service "0.5.4"]
                 [mysql/mysql-connector-java "8.0.11"]
                 [org.clojure/clojure "1.9.0"]
                 ;; [org.clojure/clojurescript "1.10.339"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :plugins [[duct/lein-duct "0.10.6"]]
  :main ^:skip-aot chat-server.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user
                         ;; :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                         }}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[eftest "0.5.2"]
                                   [integrant/repl "0.3.1"]
                                   [io.pedestal/pedestal.service-tools "0.5.4"]
                                   [kerodon "0.9.0"]]}})
