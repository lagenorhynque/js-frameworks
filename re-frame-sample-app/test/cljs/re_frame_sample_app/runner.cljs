(ns re-frame-sample-app.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [re-frame-sample-app.core-test]))

(doo-tests 're-frame-sample-app.core-test)
