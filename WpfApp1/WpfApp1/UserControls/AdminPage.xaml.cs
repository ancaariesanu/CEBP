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
    /// Interaction logic for AdminPage.xaml
    /// </summary>
    public partial class AdminPage : UserControl
    {
        public AdminPage()
        {
            InitializeComponent();
        }
        private void Sidebar_MouseEnter(object sender, System.Windows.Input.MouseEventArgs e)
        {
            Sidebar.Width = 100; 
            Sidebar.Background= new SolidColorBrush((Color)ColorConverter.ConvertFromString("#cbfb55")); 
            Dashboard.Visibility = Visibility.Visible; 
            Settings.Visibility = Visibility.Visible;
            Profile.Visibility = Visibility.Visible;
        }

     
        private void Sidebar_MouseLeave(object sender, System.Windows.Input.MouseEventArgs e)
        {
            Sidebar.Width = 50;
            Sidebar.Background =Brushes.Transparent;
            Dashboard.Visibility = Visibility.Collapsed; 
            Settings.Visibility = Visibility.Collapsed;
            Profile.Visibility = Visibility.Collapsed;
        }

        private void sidebarMenuButton(object sender, RoutedEventArgs e)
        {
            Button btn = (Button)sender;
            if (btn.Name == "Dashboard")
            {
                MessageBox.Show("pressed");
            }
            else if (btn.Name == "Settings")
            {
                MessageBox.Show("pressed");
            }
            else if (btn.Name == "Profile")
            {
                MessageBox.Show("pressed");
            }
        }
    }
}
