using FirmaTransportC_.Model;
using FirmaTransportC_.Services;

namespace FirmaTransportC_
{
    public partial class LoginForm : Form
    {
        private ITransportService service;
        private OfficeForm? officeForm;

        public LoginForm(ITransportService service)
        {
            InitializeComponent();
            this.service = service;
        }

        public void SetOfficeForm(OfficeForm officeForm)
        {
            this.officeForm = officeForm;
        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            string username = usernameTextBox.Text;
            string password = passwordTextBox.Text;

            if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
            {
                MessageBox.Show("Please enter credentials.");
                return;
            }

            try
            {
                User user = new User(-1, username, password);
                service.Login(user, officeForm);
                MessageBox.Show("Login successful!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                officeForm.SetUser(user);
                officeForm.LoadTrips();
                officeForm.Show();
                this.Hide();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
        }
    }
}
