(ns edn2yaml
  (:require [yaml.core :as yaml]))

(defn -main [& args]
  (-> (clojure.edn/read)
      (yaml/generate-string
       :dumper-options {:flow-style :block})
      println))
