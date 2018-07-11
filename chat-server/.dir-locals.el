((nil . ((cider-refresh-before-fn . "integrant.repl/suspend")
         (cider-refresh-after-fn  . "integrant.repl/resume")))
 (clojure-mode . ((eval . (define-clojure-indent
                            ;; clojure.spec
                            (fdef 1)
                            ;; test.check
                            (for-all :defn)
                            ;; re-frame
                            (reg-cofx :defn)
                            (reg-event-db :defn)
                            (reg-event-fx :defn)
                            (reg-fx :defn)
                            (reg-sub :defn))))))
