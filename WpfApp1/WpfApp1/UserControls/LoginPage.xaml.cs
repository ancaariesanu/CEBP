using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace WpfApp1.UserControls
{
    /// <summary>
    /// Interaction logic for LoginPage.xaml
    /// </summary>
    public partial class LoginPage : UserControl
    {
        public LoginPage()
        {
            InitializeComponent();
        }
        public void ChangePageFunction(object sender, RoutedEventArgs e)
        {
            GlobalVariables.ClassName = "User";
            MainWindow instance = new MainWindow();

        }
        public void eraseText(object sender, RoutedEventArgs e)
        {
            TextBox textBox = (TextBox)sender;
            textBox.Text= string.Empty;
        }
        public void getTextBack(object sender, RoutedEventArgs e)
        {
            TextBox textBox = (TextBox)sender;
            if (textBox.Text == string.Empty)
            {
                if (textBox.Name == "Username")
                {
                    textBox.Text = "USERNAME";
                }
                else 
                {
                    textBox.Text = "PASSWORD";
                }
            }
        }
        public void enterInPage(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                if (Username.Text == "admin" && Password.Text == "admin")
                {
                    MessageBox.Show("GOOD CRED", "Succes");
                    var mainwindow = (MainWindow)Application.Current.MainWindow;
                    mainwindow.FirstContent.Content = new AdminPage();
                } 
                else if (Username.Text == "user" && Password.Text == "user")
                {
                    MessageBox.Show("GOOD CRED", "Succes");
                    var mainwindow = (MainWindow)Application.Current.MainWindow;
                    mainwindow.FirstContent.Content = new UserPage();
                }
                else
                {
                    Username.Text = string.Empty;
                    Password.Text = string.Empty;
                    MessageBox.Show("BAD CRED", "Wrong input");
                }
            }
            
        }

    }
}
