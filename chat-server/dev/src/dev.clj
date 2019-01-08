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
  (duct/read-config (io/resource "chat_server/config.edn")))

(defn reset []
  (let [result (integrant.repl/reset)]
    (with-out-str (stest/instrument))
    result))

;;; unit testing

(defn test
  ([]
   (eftest/run-tests (eftest/find-tests "test")
                     {:multithread? false}))
  ([sym]
   (eftest/run-tests (eftest/find-tests sym)
                     {:multithread? false})))

;;; DB migration

(def env-profiles
  {"dev"  [:duct.profile/dev :duct.profile/local]
   "test" [:duct.profile/dev :duct.profile/test]
   "prod" [:duct.profile/prod]})

(defn- validate-env [env]
  (when-not (some #{env} (keys env-profiles))
    (throw (IllegalArgumentException. (format "env `%s` is undefined" env)))))

(defn- load-migration-config [env]
  (when-let [profiles (get env-profiles env)]
    (let [{{:keys [jdbc-url]} :duct.database.sql/hikaricp
           {:keys [migrations]} :duct.migrator/ragtime}
          (duct/prep-config (read-config) profiles)]
      {:datastore (ragtime.jdbc/sql-database jdbc-url)
       :migrations migrations})))

(defn db-migrate [env]
  (validate-env env)
  (ragtime.repl/migrate (load-migration-config env)))

(defn db-rollback [env]
  (validate-env env)
  (ragtime.repl/rollback (load-migration-config env)))

;;; namespace settings

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(def profiles
  (get env-profiles "dev"))

(integrant.repl/set-prep! #(duct/prep-config (read-config) profiles))
