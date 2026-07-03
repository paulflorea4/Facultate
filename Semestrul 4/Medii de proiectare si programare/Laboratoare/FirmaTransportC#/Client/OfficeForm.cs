using FirmaTransportC_.Model;
using FirmaTransportC_.Services;
using System.Globalization;

namespace FirmaTransportC_
{
    public partial class OfficeForm : Form, IObserver
    {
        private ITransportService service;
        private TripForm? tripForm;
        private LoginForm loginForm;
        private User? currentUser;
        public OfficeForm(ITransportService service, TripForm tripform, LoginForm loginForm)
        {
            InitializeComponent();
            this.service = service;
            this.loginForm = loginForm;
            this.tripForm = tripform;
        }

        public void SetUser(User user)
        {
            currentUser = user;
        }

        public void LoadTrips()
        {
            tripsTable.Rows.Clear();
            List<Trip> trips = service.GetAllTrips();

            foreach (Trip trip in trips)
            {
                int rowIndex = tripsTable.Rows.Add(
                    trip.Destination,
                    trip.Date,
                    trip.Hour,
                    trip.AvailableSeats
                );
                tripsTable.Rows[rowIndex].Tag = trip;
            }
        }

        private void searchTripButton_Click(object sender, EventArgs e)
        {
            string selectedDate = tripDateTimePicker.Value.ToString("yyyy-MM-dd");
            string hour = tripHourTextBox.Text.ToString();
            string destination = tripDestinationTextBox.Text;

            if (!string.IsNullOrWhiteSpace(selectedDate) && !string.IsNullOrWhiteSpace(hour) && !string.IsNullOrWhiteSpace(destination))
            {
                List<Trip> filteredTrips = service.SearchTrips(new Trip(-1, destination.Trim(), selectedDate, hour, 0));

                tripsTable.Rows.Clear();
                foreach (var trip in filteredTrips)
                {
                    int rowIndex = tripsTable.Rows.Add(
                        trip.Destination,
                        trip.Date,
                        trip.Hour,
                        trip.AvailableSeats
                    );
                    tripsTable.Rows[rowIndex].Tag = trip;
                }
            }
        }

        private void refreshTripsButton_Click(object sender, EventArgs e)
        {
            LoadTrips();
            tripDestinationTextBox.Clear();
            tripHourTextBox.Clear();
            tripDateTimePicker.Value = DateTime.Now;
        }

        private void tripsTable_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0)
            {
                return;
            }

            Trip selectedTrip = tripsTable.Rows[e.RowIndex].Tag as Trip;
            if (selectedTrip == null)
            {
                return;
            }

            if (tripForm == null || tripForm.IsDisposed)
            {
                tripForm = new TripForm(service);
            }

            tripForm.init(selectedTrip);

            if (!tripForm.Visible)
            {
                tripForm.Show(this);
            }
            else
            {
                tripForm.BringToFront();
                tripForm.Focus();
            }
        }

        public void tripsUpdated(List<Trip> trips)
        {
            if (IsDisposed || !IsHandleCreated)
            {
                return;
            }

            BeginInvoke(new MethodInvoker(delegate
            {
                if (IsDisposed)
                {
                    return;
                }

                tripsTable.Rows.Clear();
                foreach (var trip in trips)
                {
                    int rowIndex = tripsTable.Rows.Add(
                        trip.Destination,
                        trip.Date,
                        trip.Hour,
                        trip.AvailableSeats
                    );
                    tripsTable.Rows[rowIndex].Tag = trip;
                }

                if (tripForm != null && !tripForm.IsDisposed && tripForm.Visible)
                {
                    tripForm.OnTripsUpdated(trips);
                }
            }));
        }

        private void Logout()
        {
            if (currentUser == null)
            {
                MessageBox.Show(
                        "Logout failed",
                        "No user is currently logged in.",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error
                    );
                return;
            }

            try
            {
                service.Logout(currentUser);
                currentUser = null;
                MessageBox.Show("Logout successful!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                loginForm.Show();
                this.Hide();
                if (tripForm != null && !tripForm.IsDisposed)
                {
                    tripForm.Hide();
                }
                tripsTable.Rows.Clear();
                tripDestinationTextBox.Clear();
                tripHourTextBox.Clear();
                tripDateTimePicker.Value = DateTime.Now;

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            Logout();
        }

        private void OfficeForm_Load(object sender, EventArgs e)
        {

        }

        private void OfficeForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (e.CloseReason == CloseReason.UserClosing)
            {
                try
                {
                    if (currentUser != null)
                    {
                        service.Logout(currentUser);
                        currentUser = null;
                    }
                }
                catch
                { }
                finally
                {
                    if (tripForm != null && !tripForm.IsDisposed)
                    {
                        tripForm.Close();
                    }

                    if (!loginForm.IsDisposed)
                    {
                        loginForm.Close();
                    }

                    Application.Exit();
                }
            }
        }

        private void tripsTable_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}
