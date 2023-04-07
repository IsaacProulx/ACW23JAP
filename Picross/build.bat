:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: ASSIGNMENTS - CST8221 - Winter 2023
:: ---------------------------------------------------------------------
:: Begin of Script (Assignments - W23)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

SET JAVAFXDIR=%~dp0JavaFX/lib
SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=game-javac.out
SET BINERR=game-javac.err
SET JARNAME=Game.jar
SET JAROUT=game-jar.out
SET JARERR=game-jar.err
SET DOCDIR=doc
SET DOCPACK=game
SET DOCOUT=game-javadoc.out
SET DOCERR=game-javadoc.err
SET MAINCLASSSRC=src/game/Game.java
SET MAINCLASSBIN=game.Game
SET MODULELIST=javafx.controls,javafx.fxml
REM SET CLASSPATH=%CLASSPATH%;"%SRCDIR%/resources"

@echo off

ECHO "                                                                     "
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @        W I N T E R - 2 0 2 3       @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "                                                                     "

:: EXECUTION STEPS  ...................................................
:: make sure resources are accessable
robocopy "%SRCDIR%/resources" "%BINDIR%/resources" /s /e

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%;%JAVAFXDIR%/*" %MAINCLASSSRC% -d %BINDIR% > "%BINDIR%\"%BINOUT% 2> "%BINDIR%\"%BINERR%

ECHO "2. Creating Jar ..................."
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%
cd ..

ECHO "3. Creating Javadoc ..............."
javadoc -cp ".;%BINDIR%;%JAVAFXDIR%" --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > "%BINDIR%\"%DOCOUT% 2> "%BINDIR%\"%DOCERR%

ECHO "4. Running Jar ...................."
cd bin
java --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Assignments - W23)
:: ---------------------------------------------------------------------
