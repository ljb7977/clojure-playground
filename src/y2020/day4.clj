(ns y2020.day4
  (:require util))

(def input-val
  (-> (slurp "resources/y2020/day4.txt")
      (clojure.string/split #"\n\n")))
;SPEC!!!!

(def required-keys #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn ->passport-map [xs]
  "
  key:value 형태의 string의 sequence로 들어오는 입력값을 map 형태로 변환합니다.
  Input: [\"hcl:#bc352c\" \"pid:321838059\" \"byr:1930\" \"hgt:178cm\" \"cid:213\" \"eyr:2023\" \"ecl:amb\" \"iyr:2017\"]
  Output: {:byr \"1930\", :cid \"213\", :ecl \"amb\", :eyr \"2023\", :hcl \"#bc352c\", :hgt \"178cm\", :iyr \"2017\", :pid \"321838059\"}
  "
  (->> xs
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword k) v]))
       (into (sorted-map))))
;(->passport-map ["hcl:#bc352c" "pid:321838059" "byr:1930" "hgt:178cm" "cid:213" "eyr:2023" "ecl:amb" "iyr:2017"])

(defn parse [x]
  (->> x
       (map #(clojure.string/split % #"\s"))
       (map ->passport-map)))

(defn has-required-keys? [m]
  (every? (set (keys m)) required-keys))

(def valid-eye-colors
  #{:amb :blu :brn :gry :grn :hzl :oth})

(defn integer-in-range? [s start end]
  (if-let [num (Integer/parseInt s)]
    (<= start num end)
    false))

(def validators
  {:byr #(integer-in-range? % 1920 2002)
   :iyr #(integer-in-range? % 2010 2020)
   :eyr #(integer-in-range? % 2020 2030)
   :hgt #(if-let [[[_, num, unit]] (re-seq #"^(\d+)(cm|in)$" %)]
           (let [num (Integer/parseInt num)]
             (cond
               (= unit "cm") (<= 150 num 193)
               (= unit "in") (<= 59 num 76)))
           false)
   :hcl #(re-matches #"^#[a-f|0-9]{6}$" %)
   :ecl #(valid-eye-colors (keyword %))
   :pid #(re-matches #"^\d{9}$" %)
   :cid any?})

(defn get-validator-and-validate [[k v]]
  (if-let [validator (k validators)]
    (validator v)))

(defn valid-passport? [m]
  (and (has-required-keys? m)
       (every? get-validator-and-validate m)))

(comment
  ; Part 1
  (->> input-val
       parse
       (filter has-required-keys?)
       count)

  ; Part 2
  (->> input-val
       parse
       (filter valid-passport?)
       count))
