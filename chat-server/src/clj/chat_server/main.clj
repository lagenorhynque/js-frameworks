(ns chat-server.main
  (:gen-class)
  (:require [chat-server.boundary.db.core]
            [clojure.java.io :as io]
            [duct.core :as duct]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys (or (duct/parse-keys args) [:duct/daemon])]
    (-> (duct/read-config (io/resource "chat_server/config.edn"))
        (duct/prep keys)
        (duct/exec keys))))
