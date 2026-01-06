param(
  [Parameter(Mandatory=$true)][string]$TargetPath,
  [Parameter(Mandatory=$true)][string]$WorkingDirectory,
  [Parameter(Mandatory=$true)][string]$ShortcutName
)

$desktop = [Environment]::GetFolderPath('Desktop')
$lnk = Join-Path $desktop $ShortcutName

$w = New-Object -ComObject WScript.Shell
$s = $w.CreateShortcut($lnk)
$s.TargetPath = $TargetPath
$s.WorkingDirectory = $WorkingDirectory
$s.WindowStyle = 1
$s.IconLocation = $TargetPath
$s.Save()

