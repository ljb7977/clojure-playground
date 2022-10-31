(ns dev-dive.03-debug-prn)

(comment
  (set! *print-level* 2)
  (set! *print-length* 3)

  (prn *1)
  (clojure.repl/pst *e)

  ;; prn in let bindings
  (let [data (fetch-data db)
        _ (prn data)
        processed-data (process-data data)
        _ (prn processed-data)]
    processed-data)

  ;; doto prn
  (let [data (doto (fetch-data db) prn)])

  ;; doto prn in threading macro
  (-> m
      (transform)
      (transform-2)
      (doto prn)))
