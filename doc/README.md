# Allgemein #

Die Viewer App wird von sitewaerts kostenlos im Google Play Appstore bereitgestellt (Name z.B. „sitewaertsPDF“).

Umsetzung auf Basis MUPDF oder evtl. passender darauf basierender Software.

Navigation und Design nach Möglichkeit an iOS Plugin (https://github.com/sitewaerts/cordova-plugin-document-viewer) anpassen


# API #

Die App kann nur per Intent aus anderen Apps heraus gestartet werden. Über das Android-Manifest wird also nur die Activity Viewer veröffentlicht. Weitere evtl. intern benötigte Activities werden nur privat sichtbar gemacht.

App-Package: de.sitewaerts.android.pdfviewer

Haupt-Aktivität: de.sitewaerts.android.pdfviewer.Viewer

Aufruf aus anderen Apps erfolgt wie folgt:
* Intent: VIEW
* Component: de.sitewaerts.android.pdfv.Viewer
* Data: uri to file
* Type: application/pdf
* Category: CATEGORY_EMBED
* Der Aufruf erfolgt über die Methode startActivityForResult, sodass eine Benachrichtigung erfolgen kann, wenn der Viewer wieder geschlossen wird.

Versucht ein Anwender die App direkt zu starten wird lediglich eine Meldung/InfoScreen angezeigt. Schön wäre es, wenn dies einfach als HTML/CSS7IMG hinterlegt werden könnte.


#Anpassungen#

##Features entfernen##
* Button “Kette” oben rechts
* Button “Anpassungsmodus” oben rechts
* Button “mehr” oben rechts auflösen,
    - die enthaltene Funktionen “Druck” in die Hauptleiste ziehen
    - die enthaltene Funktion “Text kopieren” entfernen


##Features per Laufzeit-Konfiguration ein/ausschalten##

Vgl. dazu Javascript API des Plugins

* Druck
* Suche

Wie werden Laufzeitkonfigurationean per Intent übergeben?
* Falls notwendig, könnte vom Java-Teil des Plugins einen Konfigurationsdatei angelegt werden.
* oders die Parameter werden an die PDF-File-Url per ? angehangen


##Dokument-View##

Oben links Buttons
* Navigations-View aufrufen
    - Automatisch ausblenden bei Dokumenten mit nur einer Seite

Oben Mitte
* Dateiname (wird bei fehlendem Platz abgeschnitten)

Oben rechts Buttons
* Funktionen (Druck, Suche), ausblendbar
* Ausbaustufe: Darstellungen
    - Einzelseite
    - Doppelseite
        * Automatisch ausblenden bei Dokumenten mit nur einer Seite
    - Doppelseite mit Cover
        * Automatisch ausblenden bei Dokumenten mit nur einer Seite

Slider-Navigation unten
* Automatisch ausblenden wenn
    - Einzelansicht aktiv und Dokument nur eine Seite hat
    - Doppelansicht aktiv und Dokument nur zwei Seiten hat


##Navigations-View##

Der Button (…) oben links startet aus dem Dokument-View den (Vollbild-) Navigations-View. Über den Android- „Zurück“ Button geht’s zurück zum Dokument-View.

Im Navigationsview wird vorerst nur die Darstellung „Inhaltsbaum“ unterstützt. Die bereits in MuPDF enthaltene Ansicht sollte etwas optimiert werden, da sie sehr hässlich ist.


###!! Ausbaustufe !!###

Im Navigationsview kann über zwei Icons rechts oben zwischen verschiedenen Darstellungen umgeschaltet werden:
* Thumbs
* Inhaltsbaum (Dokument Outline)

Der Navigationsview merkt sich die zuletzt aktivierte Ansicht.


##I18n / Labels##
Fenster-Titel wird zur Laufzeit übergeben.

Keine Labels vorhanden.


