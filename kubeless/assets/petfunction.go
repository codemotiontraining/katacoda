package kubeless

import (
    "log"
    "os"
    "encoding/json"
    "github.com/kubeless/kubeless/pkg/functions"
    f "github.com/fauna/faunadb-go/v3/faunadb"
)

type Pet struct {
     Name string `fauna:"name", json:"name, omitempty`
     Age int `fauna:"age" , json:"age, omitempty`
}

func Save(event functions.Event, context functions.Context) (string, error) {
    secret := os.Getenv("FAUNA_KEY")
    dbUrl := os.Getenv("FAUNA_URL")
    client := f.NewFaunaClient(secret, f.Endpoint(dbUrl))

    log.Println(event.Data)
    var pet Pet
    json.Unmarshal([]byte(event.Data), &pet)
    client.Query(
                f.Create(
                        f.Class("pets"),
                        f.Obj{"data": pet},
                ),
    )
    res, _ := client.Query(
               f.Get(
                        f.MatchTerm(
                                f.Index("pets_by_name"),
                                pet.Name,
                        ),
                ),
     )
     var pet2 Pet
     res.At(f.ObjKey("data")).Get(&pet2)
     log.Println(pet2)
     data, _ := json.Marshal(pet2)
    return string(data), nil
}
