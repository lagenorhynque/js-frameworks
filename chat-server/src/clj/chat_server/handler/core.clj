(ns chat-server.handler.core
  (:require [ring.util.http-response :as response]
            [struct.core :as st]))

(defn validate [schemas]
  {:name ::validate
   :enter
   (fn [context]
     (let [params (get-in context [:request :params])
           route-name (get-in context [:route :route-name])
           schema (get schemas route-name [])
           [errors validated-data] (st/validate params schema
                                                {:strip true})]
       (if (empty? errors)
         (assoc-in context [:request :tx-data] validated-data)
         (assoc context :response (response/bad-request {:errors errors})))))})

(defn attach-database [db]
  {:name ::attach-database
   :enter
   (fn [context]
     (assoc-in context [:request :db] db))})
