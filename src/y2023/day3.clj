(ns y2023.day3)
(def sample-input "467..114..\n...*......\n..35..633.\n......#...\n617*......\n.....+.58.\n..592.....\n......755.\n...$.*....\n.664.598..")

(def line [[\4 \6 \7] [\. \.] [\1 \1 \4] [\. \.]])

(defn find-digits-indices [line]
  (reduce (fn [{:keys [index digits] :as acc} val]
            (let [end (+ index (count val))]
              (if (Character/isDigit (first val))
                (-> acc
                    (assoc :index end)
                    (update :digits #(assoc % val [index end])))
                (-> acc
                    (assoc :index end)))))
          {:index 0 :digits {}} line))

(comment
  
  
  (let [lines (clojure.string/split-lines sample-input)
        digits-indices (->> lines
                            (map #(partition-by (fn [s] (Character/isDigit s)) %))
                            (map find-digits-indices)
                            (map-indexed (fn [idx item] [idx (:digits item)])))]
    (for [[y digits-map] digits-indices]
      (for [[digits [start end]] digits-map]
        [(dec y) (dec start)]
        (prn y start end digits))))
  
  
  
  
  :rcf)
