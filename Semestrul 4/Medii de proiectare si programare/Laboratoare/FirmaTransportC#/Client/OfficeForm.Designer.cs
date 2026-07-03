namespace FirmaTransportC_
{
    partial class OfficeForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            tripsTable = new DataGridView();
            destinationColumn = new DataGridViewTextBoxColumn();
            dateColumn = new DataGridViewTextBoxColumn();
            hourColumn = new DataGridViewTextBoxColumn();
            availableSeatsColumn = new DataGridViewTextBoxColumn();
            tripDestinationTextBox = new TextBox();
            tripHourTextBox = new TextBox();
            tripDateTimePicker = new DateTimePicker();
            searchTripButton = new Button();
            refreshTripsButton = new Button();
            logoutButton = new Button();
            ((System.ComponentModel.ISupportInitialize)tripsTable).BeginInit();
            SuspendLayout();
            // 
            // tripsTable
            // 
            tripsTable.AllowUserToAddRows = false;
            tripsTable.AllowUserToDeleteRows = false;
            tripsTable.AllowUserToOrderColumns = true;
            tripsTable.AllowUserToResizeColumns = false;
            tripsTable.AllowUserToResizeRows = false;
            tripsTable.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            tripsTable.Columns.AddRange(new DataGridViewColumn[] { destinationColumn, dateColumn, hourColumn, availableSeatsColumn });
            tripsTable.Location = new Point(182, 68);
            tripsTable.Name = "tripsTable";
            tripsTable.Size = new Size(456, 183);
            tripsTable.TabIndex = 0;
            tripsTable.CellClick += tripsTable_CellClick;
            tripsTable.CellContentClick += tripsTable_CellContentClick;
            // 
            // destinationColumn
            // 
            destinationColumn.HeaderText = "Destination";
            destinationColumn.Name = "destinationColumn";
            // 
            // dateColumn
            // 
            dateColumn.HeaderText = "Date";
            dateColumn.Name = "dateColumn";
            // 
            // hourColumn
            // 
            hourColumn.HeaderText = "Hour";
            hourColumn.Name = "hourColumn";
            // 
            // availableSeatsColumn
            // 
            availableSeatsColumn.HeaderText = "Available Seats";
            availableSeatsColumn.Name = "availableSeatsColumn";
            // 
            // tripDestinationTextBox
            // 
            tripDestinationTextBox.Location = new Point(171, 289);
            tripDestinationTextBox.Name = "tripDestinationTextBox";
            tripDestinationTextBox.PlaceholderText = "destination";
            tripDestinationTextBox.Size = new Size(100, 23);
            tripDestinationTextBox.TabIndex = 1;
            // 
            // tripHourTextBox
            // 
            tripHourTextBox.Location = new Point(311, 289);
            tripHourTextBox.Name = "tripHourTextBox";
            tripHourTextBox.PlaceholderText = "hour";
            tripHourTextBox.Size = new Size(61, 23);
            tripHourTextBox.TabIndex = 2;
            // 
            // tripDateTimePicker
            // 
            tripDateTimePicker.Location = new Point(414, 289);
            tripDateTimePicker.Name = "tripDateTimePicker";
            tripDateTimePicker.Size = new Size(200, 23);
            tripDateTimePicker.TabIndex = 3;
            // 
            // searchTripButton
            // 
            searchTripButton.Location = new Point(355, 355);
            searchTripButton.Name = "searchTripButton";
            searchTripButton.Size = new Size(75, 28);
            searchTripButton.TabIndex = 4;
            searchTripButton.Text = "Search";
            searchTripButton.UseVisualStyleBackColor = true;
            searchTripButton.Click += searchTripButton_Click;
            // 
            // refreshTripsButton
            // 
            refreshTripsButton.Location = new Point(12, 407);
            refreshTripsButton.Name = "refreshTripsButton";
            refreshTripsButton.Size = new Size(75, 31);
            refreshTripsButton.TabIndex = 5;
            refreshTripsButton.Text = "Refresh";
            refreshTripsButton.UseVisualStyleBackColor = true;
            refreshTripsButton.Click += refreshTripsButton_Click;
            // 
            // logoutButton
            // 
            logoutButton.Location = new Point(713, 411);
            logoutButton.Name = "logoutButton";
            logoutButton.Size = new Size(75, 27);
            logoutButton.TabIndex = 7;
            logoutButton.Text = "Logout";
            logoutButton.UseVisualStyleBackColor = true;
            logoutButton.Click += logoutButton_Click;
            // 
            // OfficeForm
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(800, 450);
            Controls.Add(logoutButton);
            Controls.Add(refreshTripsButton);
            Controls.Add(searchTripButton);
            Controls.Add(tripDateTimePicker);
            Controls.Add(tripHourTextBox);
            Controls.Add(tripDestinationTextBox);
            Controls.Add(tripsTable);
            Name = "OfficeForm";
            Text = "OfficeForm";
            Load += OfficeForm_Load;
            FormClosed += OfficeForm_FormClosed;
            ((System.ComponentModel.ISupportInitialize)tripsTable).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView tripsTable;
        private DataGridViewTextBoxColumn destinationColumn;
        private DataGridViewTextBoxColumn dateColumn;
        private DataGridViewTextBoxColumn hourColumn;
        private DataGridViewTextBoxColumn availableSeatsColumn;
        private TextBox tripDestinationTextBox;
        private TextBox tripHourTextBox;
        private DateTimePicker tripDateTimePicker;
        private Button searchTripButton;
        private Button refreshTripsButton;
        private Button logoutButton;
    }
}