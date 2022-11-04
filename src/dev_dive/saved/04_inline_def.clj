(ns dev-dive.04-inline-def)

(comment
  ;; inline def
  (defn fun [numbers]
    (let [sum (apply + numbers)
          avg (/ sum (count numbers))]
      (def sum sum)
      avg)))
