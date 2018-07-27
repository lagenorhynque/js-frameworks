((nil . ((cider-refresh-before-fn . "integrant.repl/suspend")
         (cider-refresh-after-fn  . "integrant.repl/resume")))
 (clojure-mode . ((eval . (define-clojure-indent
                            ;; clojure.spec
                            (fdef 1)
                            ;; test.check
                            (for-all :defn))))))
