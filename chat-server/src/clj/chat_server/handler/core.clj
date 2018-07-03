(ns chat-server.handler.core)

(defn attach-database [db]
  {:name ::attach-database
   :enter (fn [context]
            (assoc context :db db))})
