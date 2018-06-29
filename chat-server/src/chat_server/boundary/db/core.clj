(ns chat-server.boundary.db.core
  (:require [integrant.core :as ig]
            [ragtime.jdbc :refer [load-resources]]))

(defmethod ig/prep-key :duct.migrator/ragtime [_ config]
  (assoc config :migrations (load-resources "migrations")))
