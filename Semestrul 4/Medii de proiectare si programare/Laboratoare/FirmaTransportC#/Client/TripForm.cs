using FirmaTransportC_.Model;
using FirmaTransportC_.Services;

namespace FirmaTransportC_
{
    public partial class TripForm : Form
    {
        private ITransportService service;
        private Trip? trip;

        public TripForm(ITransportService service)
        {
            InitializeComponent();
            this.service = service;
        }

        public void init(Trip trip)
        {
            this.trip = trip;
            LoadSeats();
        }

        public void LoadSeats()
        {
            seatsDataGridView.Rows.Clear();
            List<Seat> seats = service.GetSeatsForTrip(trip);
            foreach (Seat seat in seats)
            {
                int rowIndex = seatsDataGridView.Rows.Add(seat.Number, seat.ClientName ?? "-");
                seatsDataGridView.Rows[rowIndex].Tag = seat;
            }
            tripLabel.Text = $"Trip to {trip.Destination} on {trip.Date} at {trip.Hour}";
        }

        private void addReservationButton_Click(object sender, EventArgs e)
        {
            try
            {
                String clientName = clientNameTextBox.Text;
                int numberOfSeats = (int)numberOfSeatsNumericUpDown.Value;

                service.MakeReservation(new Reservation(-1, clientName, trip.Id, numberOfSeats));
                //LoadSeats();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void cancelReservationButton_Click(object sender, EventArgs e)
        {
            try
            {
                if (seatsDataGridView.CurrentCell == null)
                {
                    MessageBox.Show("Please select a seat first.", "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    return;
                }

                int rowIndex = seatsDataGridView.CurrentCell.RowIndex;
                if (rowIndex < 0)
                {
                    MessageBox.Show("Please select a seat first.", "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    return;
                }

                Seat selectedSeat = seatsDataGridView.Rows[rowIndex].Tag as Seat;
                if (selectedSeat.ReservationId == null)
                {
                    MessageBox.Show("Invalid seat selection.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                service.CancelReservation(new Reservation(selectedSeat.ReservationId.Value, selectedSeat.ClientName, selectedSeat.TripId, 0));
                //LoadSeats();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        public void OnTripsUpdated(List<Trip> trips)
        {
            if(trip == null || service == null || trips == null)
                return;

            Trip updatedTrip = trips.FirstOrDefault(t => t.Id == trip.Id);
            if (updatedTrip != null)
            {
                trip = updatedTrip;
                LoadSeats();
            }
        }

        private void TripForm_Load(object sender, EventArgs e)
        {

        }
    }
}
