using System;
using System.Diagnostics;
using System.IO;

class AutoClickerLauncher
{
    static int Main(string[] args)
    {
        try
        {
            var exeDir = AppDomain.CurrentDomain.BaseDirectory;
            var batPath = Path.Combine(exeDir, "run.bat");

            if (!File.Exists(batPath))
            {
                Console.Error.WriteLine("Nao encontrei run.bat ao lado do .exe.");
                Console.Error.WriteLine("Coloque o AutoClicker-V1.exe na mesma pasta do run.bat.");
                return 1;
            }

            var psi = new ProcessStartInfo
            {
                FileName = "cmd.exe",
                Arguments = "/c \"\"" + batPath + "\"\"",
                WorkingDirectory = exeDir,
                UseShellExecute = false,
                CreateNoWindow = false
            };

            using (var p = Process.Start(psi))
            {
                p.WaitForExit();
                return p.ExitCode;
            }
        }
        catch (Exception ex)
        {
            Console.Error.WriteLine(ex.ToString());
            return 1;
        }
    }
}

