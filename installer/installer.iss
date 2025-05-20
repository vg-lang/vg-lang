// ai bruk : var litt bruk av ai angående scripting med pascal som var noe veldig nytt for meg

#define MyAppName "VG Programming Language"
#define MyAppVersion "1.4.0"
#define MyAppExeName "VG.exe"
#define MyAppAssocName MyAppName + " File"
#define MyAppAssocExt ".vg"
#define MyAppAssocKey StringChange(MyAppAssocName, " ", "") + MyAppAssocExt

[Setup]

AppId={{77795C57-6D3D-467C-B516-2808962CBDD6}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
DefaultDirName={autopf}\{#MyAppName}
DisableDirPage=yes
ChangesAssociations=yes
DisableProgramGroupPage=yes
PrivilegesRequiredOverridesAllowed=commandline
OutputDir=C:\Users\hodif\Desktop\vgexe\installer\agrgrsfdsfd
OutputBaseFilename=VGSetup
SetupIconFile=path\to\assets\installwizard.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern


WizardImageFile=path\to\assets\sidebar.bmp

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "path\to\vg.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "path\to\vgpkg.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "path\to\JRE\*"; DestDir: "{app}\JRE"; Flags: recursesubdirs createallsubdirs
Source: "path\to\config\*"; DestDir: "{app}\config"; Flags: recursesubdirs createallsubdirs
Source: "path\to\libraries\*"; DestDir: "{app}\libraries"; Flags: recursesubdirs createallsubdirs
Source: "path\to\assets\*"; DestDir: "{app}\assets"; Flags: recursesubdirs createallsubdirs
Source: "path\to\assets\topbar.bmp"; DestDir: "{tmp}"; Flags: dontcopy 
Source: "path\to\assets\icons\vg.ico"; DestDir: "{app}\assets\icons"; Flags: ignoreversion
Source: "path\to\assets\icons\vglib.ico"; DestDir: "{app}\assets\icons"; Flags: ignoreversion
Source: "path\to\assets\icons\vgenv.ico"; DestDir: "{app}\assets\icons"; Flags: ignoreversion


[Registry]
Root: HKCU; Subkey: "Environment"; ValueType: string; ValueName: "VG_APP_CONFIG"; ValueData: "{app}\config"; Flags: uninsdeletevalue
Root: HKCU; Subkey: "Environment"; ValueType: string; ValueName: "VG_LIBRARIES_PATH"; ValueData: "{app}\libraries"; Flags: uninsdeletevalue
Root: HKCU; Subkey: "Environment"; ValueType: string; ValueName: "VG_ASSETS_PATH"; ValueData: "{app}\assets"; Flags: uninsdeletevalue



Root: HKCR; Subkey: ".vg"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocKey}vg"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}vg"; ValueType: string; ValueName: ""; ValueData: "{#MyAppName} File (.vg)"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}vg\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\assets\icons\vg.ico"
Root: HKCR; Subkey: "{#MyAppAssocKey}vg\shell\open\command"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName} %1"

Root: HKCR; Subkey: ".vglib"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocKey}vglib"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}Vglib"; ValueType: string; ValueName: ""; ValueData: "{#MyAppName} File (.vglib)"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}Vglib\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\assets\icons\vglib.ico"
Root: HKCR; Subkey: "{#MyAppAssocKey}Vglib\shell\open\command"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName} %1"

Root: HKCR; Subkey: ".vgenv"; ValueType: string; ValueName: ""; ValueData: "{#MyAppAssocKey}vgenv"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}vgenv"; ValueType: string; ValueName: ""; ValueData: "{#MyAppName} Environment File (.vgenv)"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#MyAppAssocKey}vgenv\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\assets\icons\vgenv.ico"
Root: HKCR; Subkey: "{#MyAppAssocKey}vgenv\shell\open\command"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppExeName} %1"

Root: HKCR; Subkey: "Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".vg"; ValueData: ""
Root: HKCR; Subkey: "Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".vglib"; ValueData: ""
Root: HKCR; Subkey: "Applications\{#MyAppExeName}\SupportedTypes"; ValueType: string; ValueName: ".vgenv"; ValueData: ""



[Code]
procedure AddToSystemPath();
var
  OldPath, NewPath, AppDir, JreBinDir, JarDir, ConfigDir, LibrariesDir: string;
begin
  AppDir := ExpandConstant('{app}');
  JreBinDir := ExpandConstant('{app}\jre\bin');
  JarDir := ExpandConstant('{app}\jar');
  ConfigDir := ExpandConstant('{app}\config');
  LibrariesDir := ExpandConstant('{app}\libraries');

  if RegQueryStringValue(
       HKEY_LOCAL_MACHINE,
       'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
       'Path',
       OldPath
     ) then
  begin
    if Pos(AppDir, OldPath) = 0 then OldPath := OldPath + ';' + AppDir;
    if Pos(JreBinDir, OldPath) = 0 then OldPath := OldPath + ';' + JreBinDir;
    if Pos(JarDir, OldPath) = 0 then OldPath := OldPath + ';' + JarDir;
    if Pos(ConfigDir, OldPath) = 0 then OldPath := OldPath + ';' + ConfigDir;
    if Pos(LibrariesDir, OldPath) = 0 then OldPath := OldPath + ';' + LibrariesDir;

    RegWriteStringValue(
      HKEY_LOCAL_MACHINE,
      'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
      'Path',
      OldPath
    );
  end;

  RegWriteStringValue(
    HKEY_LOCAL_MACHINE,
    'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
    'VG_CONFIG_PATH',
    ConfigDir
  );
  RegWriteStringValue(
    HKEY_LOCAL_MACHINE,
    'SYSTEM\CurrentControlSet\Control\Session Manager\Environment',
    'VG_LIBRARIES_PATH',
    LibrariesDir
  );
end;

procedure RefreshEnvironment();
var
  ErrorCode: Integer;
begin
  Exec('C:\Windows\System32\cmd.exe', '/C setx PATH "%PATH%"', '',
       SW_HIDE, ewWaitUntilTerminated, ErrorCode);
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    AddToSystemPath();
    RefreshEnvironment();
  end;
end;


procedure InitializeWizard();
var
  BitmapImage: TBitmapImage;
begin
  ExtractTemporaryFile('topbar.bmp');
  BitmapImage := TBitmapImage.Create(WizardForm);
  BitmapImage.Parent := WizardForm.MainPanel;
  BitmapImage.Width := WizardForm.MainPanel.Width;
  BitmapImage.Height := WizardForm.MainPanel.Height;
  BitmapImage.Anchors := [akLeft, akTop, akRight, akBottom];
  BitmapImage.Stretch := True;
  BitmapImage.AutoSize := False;
  BitmapImage.Bitmap.LoadFromFile(ExpandConstant('{tmp}\topbar.bmp'));

  WizardForm.WizardSmallBitmapImage.Visible := False;
  WizardForm.PageDescriptionLabel.Visible := False;
  WizardForm.PageNameLabel.Visible := False;
end;

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; WorkingDir: "{app}"
Name: "{autoprograms}\{#MyAppName} Package Manager"; Filename: "{app}\vgpkg.exe"; WorkingDir: "{app}"


