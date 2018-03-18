# Utiliser Suricat

## Structure

Le dossier content le fichier `suricat.jar` doit avoir la structure suivante :

```bash
.
|_ [...]
|_ README.pdf
|_ suricat.bat
|_ suricat.jar
|_ suricat.sh
|_ [...]
```



## Attention

Les symboles `>  ` servent à indiquer le début d'une ligne dans un fichier, ils ne sont donc pas à recopier



## Sous UNIX (ou compatible Bash)

Éditer le fichier `~/.bashrc` (ou un quelconque fichier `souce`'d par celui-ci') pour y ajouter la ligne suivante :

```bash
> alias suricat="/mon/chemin/absolu/vers/suricat.sh"
```

Ouvrez un nouveau terminal et testez avec la commande suivante :

```bash
suricat --help
```



## Windows (Batch)

Créer un fichier (que nous appelerons `suricat.doskey` par la suite) et y inscrire la ligne suivante :

```basic
> suricat = "mon/chemin/absolu/vers/suricat.bat" %*
```

Puis créer un fichier (que nous appelerons `suricat.cmd` par la suite) et y inscrire les lignes suivantes :

```basic
> @echo off
> cls
> doskey /macrofile="mon/chemin/absolu/vers/suricat.doskey"
```

Enfin, créer un fichier (que nous appelerons `suricat.reg`) et y inscrire les lignes suivantes :

```basic
> Windows Registry Editor Version 5.00
> [HKEY_CURRENT_USER\SOFTWARE\Microsoft\Command Processor] "Autorun"="mon/chemin/absolu/vers/suricat.cmd"
```



Pour l'utiliser de suite, lancer `suricat.cmd` dans un terminal. Cependant il est conseillé de redémarrer votre ordinateur (pour ne pas avoir à relancer `suricat.cmd` dans chaque terminal).