(ns generate-migration
  (:require [clojure.java.io :as io]))

(def migrations-dir "resources/migrations")

(defn -main [migration-name]
  (let [fnames (.list (io/file migrations-dir))
        migration-number (if-let [fname (->> fnames sort reverse first)]
                           (-> (re-matches #"(\d+).*" fname)
                               second
                               (#(Integer/parseInt %))
                               inc)
                           1)
        new-fname (format "%03d-%s" migration-number migration-name)]
    (doseq [extension [".up.sql" ".down.sql"]]
      (let [file (str migrations-dir \/ new-fname extension)]
        (spit file nil)
        (println "Generated:" file)))))
