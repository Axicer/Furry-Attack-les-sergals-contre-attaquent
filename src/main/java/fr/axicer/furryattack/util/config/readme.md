
# Configuration

## Lecture de valeur

Pour lire une valeur utilisez:  
`config.getXXX(String path);`

## Ecriture de valeur

Pour ecrire une valeur utilisez:  
`config.setXXX(String path, XXX value, boolean force);`
  
Variables:
- `path`: chemin d'accés a la variable (voir partie )
- `value`: la valeur à donner
- `force`: boolean indiquant si on souhaite forcer la creation si le chemin d'accés n'existe pas  

## gestion du path

### Ecriture general

Le chemin d'accés est toujours sous la forme:  
`chemin.vers.ma.variable`

### Variante pour l'ecriture

Lors de l'ajout d'une variable avec `force` activé, le chemin d'acces se fera avec un objet.  
Mais si on souhaite un tableau, il est possible de le preciser en ajoutant un `[` en debut de nom.  

Exemple:  

`config.setString("chemin.valeurs.0", "test", true);`  
créera:  
```JSON
{
	"chemin":{
		"valeurs":{
			"0":"test"
		}
	}
}
```
Alors que:
`config.setString("chemin.[valeurs.0", "test", true);`(notez l'ajout de `[` avant `valeurs`)
créera:
```JSON
{
	"chemin":{
		"valeurs":[
			"test"
		]
	}
}
```