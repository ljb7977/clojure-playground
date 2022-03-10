(ns sample)

" GraphQL 쿼리 생김새:
query {
  vehicle {
    id
    name
    year
    price
  }
}

DB 스키마
(table vehicle
 id int
 name varchar
 madeAt int
 price double
)

sqlmap
{:select [:id :name :year :price]
 :from vehicle}
"

clojure.string/split-lines
(defn fetch-vehicle [db fields]
  (fetch db {:select [fields]
             :from :vehicle}))

(defn resolver [ctx arg parent]
  (let [fields (selection-seq ctx)
        fields->columns {:year :made-at}]
    ()
    (fetch-vehicle db fields)))


(defn resolver [ctx arg parent]
  (let [a-id (:a-id arg)
        a-from-db (fetch-a-from-db db arg)
        b-from-db (fetch-b-from-db db (map :b-id a-from-db))
        ; 윗줄에서 (map :b-id a-from-db)가 빈 리스트인지 아닌지 검사하고 넣어 준다

        arguments-to-fetch-c (some-function a-from-db b-from-db)
        c-from-db (fetch-c-from-db db arguments-to-fetch-c)]
        ; 여기서 some-function의 결과가 뭐가 나올지 다 검사하고 넣는다?
    c-from-db))

(defn a-from-db [db a-id]
  (fetch! db {:select [:a-id :b-id]
              :from :a
              :where [:= :id a-id]}))

(defn b-from-db [db b-ids]
  (fetch! db {:select :*
              :from :b
              :where [:in :id b-ids]}))
