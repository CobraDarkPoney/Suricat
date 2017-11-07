# Informations concernant le fichier JSON #

Pour qu'un fichier JSON décrivant une transaction d'une base de donnée soit considéré comme valide pour ce logiciel et qu'il puisse donc être utilisé sans accroc il faut respecter la syntaxe suivante :

```json
{
  "models": [],
  "transactions": []
}
```

## Models ##

Nous considérons ainsi que la partie `models` décrit les tables utilisées dans ladite transaction, ainsi un `model` doit respecter la syntaxe suivante :

```json
{
  "name": "users",
  "attributes": []
}
```

Au sein d'un `model`, le nom (`name`) est représenté par une chaîne de caractères et les attributs (`attributes`) sont représentés sous formes d'objets (`attributes` est donc un tableau d'objets).

La syntaxe correspondant à un attribut est la suivante :

```json
{
  "name": "users.username",
  "dbtype": "VARCHAR"
}
```

Ainsi, au sein d'un `attribute`, nous représentons le nom de celui-ci sous la forme d'une chaîne de caractère (**Attention:** on utilise pour le nom une forme dite "namespacée" suivant le format `namespace.name` soit ici `modelName.ttributeName` ).



### Exemple ###

Ainsi, on peut écrire `models` de la façon suivante :

```json
"models": [
  {
    "name": "users",
    "attributes": [
      {
        "name": "id",
        "dbtype": "NUMBER"
      },
      {
        "name": "users.username",
        "dbtype": "VARCHAR"
      },
      {
        "name": "users.password_hash",
        "dbtype": "VARCHAR"
      },
      {
        "name": "users.email",
        "dbtype": "VARCHAR"
      }
    ]
  },
  {
    "name": "user_permissions",
    "attributes": [
      {
        "name": "user_permissions.uid",
        "dbtype": "NUMBER"
      },
      {
        "name": "isadmin",
        "dbtype": "TINYINT"
      }
    ]
  }
]
```

## Transactions ##

Nous considérons ainsi que la partie `transactions` représente l'ensemble des étapes/actions/opérations d'une transaction.

Chaque `transaction` (action d'une transaction, sous-partie de `transactions`) doit respecter la syntaxe suivante :

```json
{
  "source": "t1",
  "index": 1,
  "lock": "XE",
  "type": "find",
  "target": "users"
}
```

Ici, `source` désigne l'origine (ou l'auteur) de l'action (représenté sous forme de chaîne de caractère).

La propriété `index` représente un entier (unique) indiquant la "position" de l'action dans l'exécution de la transaction. Ainsi, plus `index` est proche de 0 et plus l'action sera effectuée tôt, plus `index` est proche de `Integer.MAX_INT` (2^32-1) plus cette action sera effctuée tardivement.



La propriété `lock` désigne le verrour demandé par la transaction, les différents types disponibles seront énoncés par la suite.



La propriété `type` décrit le type/but de l'opération, de même que pour `lock`, les différents types disponibles seront énoncés par la suite.



La propriété `target` désigne le granule (désigné par sa propriété `name` dans le JSON) concerné par l'opération.

### Lock ###

Les types de verrous suivants sont disponibles :

* **XE** - Verrou exclusif étendu
* **X** - Verrou exclusif
* **UE** - Verrou de mise à jour étendu
* **U** - Verrou de mise à jour
* **SE** - Verrou partagé étendu
* **S** - Verrou partagé
* **NONE** - Aucun verrou



Il faut cependant bien veiller à respecter la casse : **XE** donnera `"lock": "XE"` dans le fichier JSON.



###### Remarque ######

Il est tout à fait possible d'indiquer `"lock": ""` , cela sera aussitôt détecté et remplacé par un verrou **NONE** au sein du logiciel.



### Type ### 

Les types d'opérations suivants sont disponibles:

* **find** - opération de lecture
* **upd** - opération de mise à jour
* **rollback** - opération d'annulation de transaction
* **commit** - opération de validation de transaction



De même que pour `lock`, un respect de la casse est requis : **upd** donnera `"type": "upd"` dans le fichier JSON.



### Target ###

La propriété `target` est relativement particulière :  en effet, le JSON est un formalisme ayant pour simple but la description et celui-ci ne permet pas un système de référence (faire référence à une propriété déjà existante ou future).

Ainsi, il faut se contenter de recopier correctement le contenu de la propriété `name` du granule concerné.



### Granule ###

Dans le cadre de ce logiciel, nous ne considérons que deux types de granules :

* une table (un `model`)
* un attribut d'une table (un `attribute`)



### Exemple ###

Ainsi, on pourrait écrire `transactions` de la façon suivante :

```json
"transactions": [
  {
    "source": "t1",
    "index": 1,
    "lock": "NONE",
    "type": "find",
    "target": "users"
  },
  {
    "source": "t2",
    "index": 2,
    "lock": "UE",
    "type": "find",
    "target": "users"
  },
  {
    "source": "t1",
    "index": 3,
    "lock": "UE",
    "target": "users"
  }
]
```



## Exemple final : exemple de fichier JSON ##

En reprenant les divers exemples précédents, on peut former le fichier JSON suivant :

```json
{
  "models": [
    {
      "name": "users",
      "attributes": [
        {
          "name": "id",
          "dbtype": "NUMBER"
        },
        {
          "name": "users.username",
          "dbtype": "VARCHAR"
        },
        {
          "name": "users.password_hash",
          "dbtype": "VARCHAR"
        },
        {
          "name": "users.email",
          "dbtype": "VARCHAR"
        }
      ]
    },
    {
      "name": "user_permissions",
      "attributes": [
        {
          "name": "user_permissions.uid",
          "dbtype": "NUMBER"
        },
        {
          "name": "isadmin",
          "dbtype": "TINYINT"
        }
      ]
    }
  ],
  "transactions": [
    {
      "source": "t1",
      "index": 1,
      "lock": "NONE",
      "type": "find",
      "target": "users"
    },
    {
      "source": "t2",
      "index": 2,
      "lock": "UE",
      "type": "find",
      "target": "users"
    },
    {
      "source": "t1",
      "index": 3,
      "lock": "UE",
      "target": "users"
    }
  ]
}
```

