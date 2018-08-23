(defproject chat-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [clojure.java-time "0.3.2"]
                 [com.stuartsierra/dependency "0.2.0"]
                 [duct/core "0.6.2"]
                 [duct/database.redis.carmine "0.1.1"]
                 [duct/database.sql.hikaricp "0.3.3"]
                 [duct/module.logging "0.3.1"]
                 [duct/module.sql "0.4.2"]
                 [funcool/struct "1.3.0"]
                 [honeysql "0.9.3"]
                 [integrant "0.7.0-alpha2"]
                 [io.pedestal/pedestal.jetty "0.5.4"]
                 [io.pedestal/pedestal.service "0.5.4"]
                 [metosin/ring-http-response "0.9.0"]
                 [mysql/mysql-connector-java "8.0.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :plugins [[duct/lein-duct "0.10.6"]]
  :main ^:skip-aot chat-server.main
  :source-paths   ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :profiles
  {:repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :dev  [:shared :project/dev :profiles/dev]
   :test [:shared :project/dev :project/test :profiles/test]
   :uberjar [:shared :project/uberjar]

   :shared {}
   :project/dev {:source-paths   ["dev/src"]
                 :resource-paths ["dev/resources"]
                 :dependencies   [[clj-http "3.9.1"]
                                  [com.bhauman/rebel-readline "0.1.4"]
                                  [com.gearswithingears/shrubbery "0.4.1"]
                                  [eftest "0.5.2"]
                                  [integrant/repl "0.3.1"]
                                  [io.pedestal/pedestal.service-tools "0.5.4"]
                                  [kerodon "0.9.0"]]
                 :plugins [[jonase/eastwood "0.2.9"]
                           [lein-cljfmt "0.6.0"]
                           [lein-cloverage "1.0.11"]
                           [lein-codox "0.10.4"]
                           [lein-eftest "0.5.2"]
                           [lein-kibit "0.1.6"]]
                 :aliases {"db-migrate" ^{:doc "Migrate DB to the latest migration."}
                           ["run" "-m" "dev/db-migrate"]
                           "db-rollback" ^{:doc "Rollback DB one migration."}
                           ["run" "-m" "dev/db-rollback"]
                           "rebel" ^{:doc "Run REPL with rebel-readline."}
                           ["trampoline" "run" "-m" "rebel-readline.main"]
                           "lint" ^{:doc "Execute cljfmt check, eastwood and kibit."}
                           ["do"
                            ["cljfmt" "check"]
                            ["eastwood" "{:config-files [\"dev/resources/eastwood_config.clj\"]
                                          :test-paths []}"]
                            ["kibit"]]}
                 :cljfmt {:indents {fdef [[:inner 0]]
                                    for-all [[:inner 0]]}}}
   :project/test {}
   :project/uberjar {:aot :all
                     :uberjar-name "chat-server.jar"}
   :profiles/dev {}
   :profiles/test {}})
