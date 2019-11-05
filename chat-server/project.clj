(defproject chat-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.8.1"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [duct.module.cambium "0.1.0"]
                 [duct.module.pedestal "2.0.2" :exclusions [ring/ring-core]]
                 [duct/core "0.7.0"]
                 [duct/database.redis.carmine "0.1.1"]
                 [duct/module.sql "0.5.0"]
                 [funcool/struct "1.4.0" :exclusions [org.clojure/clojurescript]]
                 [honeysql "0.9.8"]
                 [metosin/ring-http-response "0.9.1"]
                 [mysql/mysql-connector-java "8.0.18"]
                 [org.clojure/clojure "1.10.1"]]
  :plugins [[duct/lein-duct "0.12.1"]]
  :middleware [lein-duct.plugin/middleware]
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
                 :dependencies   [[clj-http "3.10.0" :exclusions [potemkin]]
                                  [com.bhauman/rebel-readline "0.1.4"]
                                  [com.gearswithingears/shrubbery "0.4.1"]
                                  [eftest "0.5.9" :exclusions [fipp
                                                               org.clojure/core.rrb-vector]]
                                  [integrant/repl "0.3.1"]
                                  [orchestra "2019.02.06-1"]
                                  [pjstadig/humane-test-output "0.10.0"]]
                 :plugins [[jonase/eastwood "0.3.6"]
                           [lein-cljfmt "0.6.4"]
                           [lein-cloverage "1.1.2"]
                           [lein-codox "0.10.7"]
                           [lein-kibit "0.1.7"]]
                 :aliases {"db-migrate" ^{:doc "Migrate DB to the latest migration."}
                           ["run" "-m" "dev/db-migrate"]
                           "db-rollback" ^{:doc "Rollback DB one migration."}
                           ["run" "-m" "dev/db-rollback"]
                           "rebel" ^{:doc "Run REPL with rebel-readline."}
                           ["trampoline" "run" "-m" "rebel-readline.main"]
                           "test-coverage" ^{:doc "Execute cloverage."}
                           ["cloverage" "--ns-exclude-regex" "^(:?dev|user)$" "--codecov" "--junit"]
                           "lint" ^{:doc "Execute cljfmt check, eastwood and kibit."}
                           ["do"
                            ["cljfmt" "check"]
                            ["eastwood" "{:config-files [\"dev/resources/eastwood_config.clj\"]
                                          :source-paths [\"src/clj\"]
                                          :test-paths []}"]
                            ["kibit"]]}
                 :injections [(require 'pjstadig.humane-test-output)
                              (pjstadig.humane-test-output/activate!)]
                 :cljfmt {:indents {fdef [[:inner 0]]
                                    for-all [[:inner 0]]}}
                 :codox {:output-path "target/codox"
                         :source-uri "https://github.com/lagenorhynque/js-frameworks/blob/master/chat-server/{filepath}#L{line}"
                         :metadata {:doc/format :markdown}}}
   :project/test {}
   :project/uberjar {:aot :all
                     :uberjar-name "chat-server.jar"}
   :profiles/dev {}
   :profiles/test {}})
