$files = Get-ChildItem -Filter *.png
foreach ($file in $files) {
    $currentNumber = [int]($file.BaseName -replace '\D')
    $newNumber = $currentNumber - 1
    $newFileName = "tile{0:D3}.png" -f $newNumber
    Rename-Item -Path $file.FullName -NewName $newFileName -Force
}
