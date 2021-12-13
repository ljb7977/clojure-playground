(ns y2020.day4
  (:require util))

(def input-val
  ;(util/read-lines "y2020/day4-example.txt")
  (util/read-lines "y2020/day4.txt"))

(def valid-passport-keys
  #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn split-strings-by-empty-one [xs]
  "string들의 sequence에서, 빈 문자열을 기준으로 string들을 나누어 각각 한 string으로 합친 것들의 리스트를 반환합니다.
  Input:
  (
  [\"ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\"]
  [\"byr:1937 iyr:2017 cid:147 hgt:183cm\"]
  [\"\"]
  [\"iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\"]
  [\"hcl:#cfa07d byr:1929\"]

  Output:s
  [\"ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm\"
   \"iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929\"]
  "
  (->> xs
       (reduce (fn [{:keys [result prev]} curr]
                 (if (= "" curr) {:result (conj result prev) :prev ""}
                                 {:result result :prev (str prev curr " ")}))
               {:result [] :prev ""})
       :result))
; split-with을 써서 할 수 있는 방법이 어떻게 될까?

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
       vec
       (#(conj % ""))
       split-strings-by-empty-one
       (map #(clojure.string/split % #" "))
       (map ->passport-map)))

(defn has-valid-keys? [m]
  (every? (set (keys m)) valid-passport-keys))

(def valid-eye-colors
  #{:amb :blu :brn :gry :grn :hzl :oth})

(def validators
  {:byr #(if-let [num (Integer/parseInt %)]
            (<= 1920 num 2002)
            false)
   :iyr #(if-let [num (Integer/parseInt %)]
            (<= 2010 num 2020)
            false)
   :eyr #(if-let [num (Integer/parseInt %)]
            (<= 2020 num 2030)
            false)
   :hgt #(if-let [[[_, num, unit]] (re-seq #"^(\d+)(cm|in)$" %)]
           (let [num (Integer/parseInt num)]
             (cond
               (= unit "cm") (<= 150 num 193)
               (= unit "in") (<= 59 num 76)))
           false)
   :hcl #(if-let [[color] (re-seq #"^#[a-f|0-9]{6}$" %)]
           true
           false)
   :ecl #(valid-eye-colors (keyword %))
   :pid #(if-let [[num] (re-seq #"^\d{9}$" %)]
           true
           false)})

(defn get-validator-and-validate [[k v]]
  (if-let [validator (get validators k)]
    (validator v)
    true))

(defn valid-passport? [m]
  (and (has-valid-keys? m)
       (->> m
            seq
            (every? get-validator-and-validate))))

;(valid-passport?
;  {:byr "1925", :ecl "gry", :eyr "2024", :hcl "#ceb3a1", :hgt "173cm", :iyr "2013", :pid "070222017"})

(comment
  ; Part 1
  (->> input-val
       parse
       (map has-valid-keys?)
       (filter true?)
       count)

  ; Part 2
  (->> input-val
       parse
       (map valid-passport?)
       (filter true?)
       count))
