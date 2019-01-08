(ns chat-server.main
  (:gen-class)
  (:require [chat-server.boundary.db.core]
            [duct.core :as duct]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (duct/resource "chat_server/config.edn")
        duct/read-config
        (duct/exec-config profiles keys))))
