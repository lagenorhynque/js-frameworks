(ns edn2json
  (:require [cheshire.core :as cheshire]))

(defn -main [& args]
  (-> (clojure.edn/read)
      (cheshire/generate-string {:pretty true})
      println))
