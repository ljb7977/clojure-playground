(ns y2022.day11)

(def sample-monkeys
  [{:items [79 98]
    :operation #(* % 19)
    :test #(if (= (rem % 23) 0)
             2 3)}
   {:items [54 65 75 74]
    :operation #(+ % 6)
    :test #(if (= (rem % 19) 0)
             2 0)}
   {:items [79 60 97]
    :operation #(* % %)
    :test #(if (= (rem % 13) 0)
             1 3)}
   {:items [74]
    :operation #(+ % 3)
    :test #(if (= (rem % 17) 0)
             0 1)}])

(def divider (* 2 3 5 7 11 13 17 19))

(def monkeys
  [{:items [89 73 66 57 64 80]
    :operation #(* % 3)
    :test #(if (zero? (rem % 13)) 6 2)}
   {:items [83, 78, 81, 55, 81, 59, 69]
    :operation #(+ % 1)
    :test #(if (zero? (rem % 3)) 7 4)}
   {:items [76 91 58 85]
    :operation #(* % 13)
    :test #(if (zero? (rem % 7)) 1 4)}
   {:items [71 72 74 76 68]
    :operation #(* % %)
    :test #(if (zero? (rem % 2)) 6 0)}
   {:items [98 85 84]
    :operation #(+ % 7)
    :test #(if (zero? (rem % 19)) 5 7)}
   {:items [78]
    :operation #(+ % 8)
    :test #(if (zero? (rem % 5)) 3 0)}
   {:items [86, 70, 60, 88, 88, 78, 74, 83]
    :operation #(+ % 4)
    :test #(if (zero? (rem % 11)) 1 2)}
   {:items [81 58]
    :operation #(+ % 5)
    :test #(if (zero? (rem % 17)) 3 5)}])

(defn process-monkey [monkeys index reduce-worry-level]
  (let [{:keys [items operation test]} (nth monkeys index)
        item-to-next-monkey (->> items
                                 (map operation)
                                 (map reduce-worry-level)
                                 (map (juxt identity test)))
        monkeys (-> monkeys
                    (assoc-in [index :items] [])
                    (update-in [index :count] + (count items)))
        monkeys (assoc-in monkeys [index :items] [])]  ;; clear current monkey's item
    (reduce (fn [acc [item next-monkey]]
              (update-in acc [next-monkey :items] conj item))
            monkeys
            item-to-next-monkey)))

(defn process-round [reduce-worry-level monkeys]
  (->> (range (count monkeys))
       (reduce (fn [acc index]
                 (process-monkey acc index reduce-worry-level))
               monkeys)))

(comment
  ;; Part 1
  (->> monkeys
       (mapv #(assoc % :count 0))
       (iterate (partial process-round #(quot % 3)))
       (drop 20)
       first
       (sort-by :count >)
       (take 2)
       (map :count)
       (apply *))
  := 119715

  ;; Part 2
  (->> monkeys
       (mapv #(assoc % :count 0))
       (iterate (partial process-round #(rem % divider)))
       (drop 10000)
       first
       (sort-by :count >)
       (take 2)
       (map :count)
       (apply *))
  := 18085004878)
