:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: ASSIGNMENTS - CST8221 - Winter 2023
:: ---------------------------------------------------------------------
:: Begin of Script (Assignments - W23)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

SET JAVAFXDIR=JavaFX/lib
SET BINDIR=bin
SET JARNAME=Game.jar
SET MODULELIST=javafx.controls,javafx.fxml

@echo off

:: EXECUTION STEPS  ...................................................

ECHO "Starting App"

echo

java --module-path %JAVAFXDIR% --add-modules %MODULELIST% -jar "%BINDIR%/"%JARNAME%

@echo on

:: ---------------------------------------------------------------------
:: End of Script (Assignments - W23)
:: ---------------------------------------------------------------------
