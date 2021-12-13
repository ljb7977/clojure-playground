(ns y2020.day4
  (:require util))

(def input-val
  ;(util/read-lines "y2020/day4-example.txt")
  (util/read-lines "y2020/day4.txt"))

(def valid-passport-keys
  #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn split-strings-by-empty-one [xs]
  (->> xs
       (reduce (fn [{:keys [result prev]} curr]
                 (if (= "" curr) {:result (conj result prev) :prev ""}
                                 {:result result :prev (str prev curr " ")}))
               {:result [] :prev ""})
       :result))
; split-with을 써서 할 수 있는 방법이 어떻게 될까?

(defn ->passport-map [xs]
  (->> xs
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword k) v]))
       (into (sorted-map))))
(->passport-map ["hcl:#bc352c" "pid:321838059" "byr:1930" "hgt:178cm" "cid:213" "eyr:2023" "ecl:amb" "iyr:2017"])

(defn parse [x]
  (->> x
       vec
       (#(conj % ""))
       split-strings-by-empty-one
       (map #(clojure.string/split % #" "))
       (map ->passport-map)))

(defn validate-passport [m]
  (every? (set (keys m)) valid-passport-keys))

(comment
  (->> input-val
       parse
       (map validate-passport)
       (filter true?)
       count))
