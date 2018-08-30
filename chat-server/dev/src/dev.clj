(ns dev
  (:refer-clojure :exclude [test])
  (:require [chat-server.boundary.db.core]
            [clojure.java.io :as io]
            [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.tools.namespace.repl :refer [refresh]]
            [duct.core :as duct]
            [duct.core.repl :as duct-repl]
            [eftest.runner :as eftest]
            [fipp.edn :refer [pprint]]
            [integrant.core :as ig]
            [integrant.repl :refer [clear halt go init prep]]
            [integrant.repl.state :refer [config system]]
            [orchestra.spec.test :as stest]
            [ragtime.jdbc]
            [ragtime.repl]))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "dev.edn")))

(defn test
  ([]
   (eftest/run-tests (eftest/find-tests "test")
                     {:multithread? false}))
  ([sym]
   (eftest/run-tests (eftest/find-tests sym)
                     {:multithread? false})))

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(integrant.repl/set-prep! (comp ig/prep duct/prep read-config))

(defn reset []
  (integrant.repl/reset)
  (with-out-str (stest/instrument))
  nil)

;;; DB migration

(def env-files
  {"dev" "dev.edn"
   "test" "test.edn"
   "prod" "chat_server/config.edn"})

(defn- validate-env [env]
  (when-not (some #{env} (keys env-files))
    (throw (IllegalArgumentException. (format "env `%s` is undefined" env)))))

(defn- load-migration-config [env]
  (when-let [f (get env-files env)]
    (let [{{:keys [database-url]} :duct.module/sql
           {:keys [migrations]} :duct.migrator/ragtime}
          (-> (duct/read-config (io/resource f))
              duct/prep
              ig/prep)]
      {:datastore (ragtime.jdbc/sql-database database-url)
       :migrations migrations})))

(defn db-migrate [env]
  (validate-env env)
  (ragtime.repl/migrate (load-migration-config env)))

(defn db-rollback [env]
  (validate-env env)
  (ragtime.repl/rollback (load-migration-config env)))
