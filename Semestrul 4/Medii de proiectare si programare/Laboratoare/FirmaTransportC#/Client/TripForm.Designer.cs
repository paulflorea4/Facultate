namespace FirmaTransportC_
{
    partial class TripForm
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
            cancelReservationButton = new Button();
            seatsDataGridView = new DataGridView();
            seatNumberColumn = new DataGridViewTextBoxColumn();
            seatClientNameColumn = new DataGridViewTextBoxColumn();
            clientNameTextBox = new TextBox();
            numberOfSeatsNumericUpDown = new NumericUpDown();
            label1 = new Label();
            addReservationButton = new Button();
            tripLabel = new Label();
            ((System.ComponentModel.ISupportInitialize)seatsDataGridView).BeginInit();
            ((System.ComponentModel.ISupportInitialize)numberOfSeatsNumericUpDown).BeginInit();
            SuspendLayout();
            // 
            // cancelReservationButton
            // 
            cancelReservationButton.Location = new Point(109, 348);
            cancelReservationButton.Name = "cancelReservationButton";
            cancelReservationButton.Size = new Size(127, 23);
            cancelReservationButton.TabIndex = 0;
            cancelReservationButton.Text = "Cancel Reservation";
            cancelReservationButton.UseVisualStyleBackColor = true;
            cancelReservationButton.Click += cancelReservationButton_Click;
            // 
            // seatsDataGridView
            // 
            seatsDataGridView.AllowUserToResizeColumns = false;
            seatsDataGridView.AllowUserToResizeRows = false;
            seatsDataGridView.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            seatsDataGridView.Columns.AddRange(new DataGridViewColumn[] { seatNumberColumn, seatClientNameColumn });
            seatsDataGridView.Location = new Point(12, 61);
            seatsDataGridView.Name = "seatsDataGridView";
            seatsDataGridView.RowHeadersWidth = 62;
            seatsDataGridView.Size = new Size(337, 246);
            seatsDataGridView.TabIndex = 1;
            // 
            // seatNumberColumn
            // 
            seatNumberColumn.HeaderText = "Seat Number";
            seatNumberColumn.MinimumWidth = 8;
            seatNumberColumn.Name = "seatNumberColumn";
            seatNumberColumn.Width = 150;
            // 
            // seatClientNameColumn
            // 
            seatClientNameColumn.HeaderText = "Client Name";
            seatClientNameColumn.MinimumWidth = 8;
            seatClientNameColumn.Name = "seatClientNameColumn";
            seatClientNameColumn.Width = 150;
            // 
            // clientNameTextBox
            // 
            clientNameTextBox.Location = new Point(432, 125);
            clientNameTextBox.Name = "clientNameTextBox";
            clientNameTextBox.PlaceholderText = "name";
            clientNameTextBox.Size = new Size(151, 23);
            clientNameTextBox.TabIndex = 2;
            // 
            // numberOfSeatsNumericUpDown
            // 
            numberOfSeatsNumericUpDown.Location = new Point(535, 180);
            numberOfSeatsNumericUpDown.Maximum = new decimal(new int[] { 18, 0, 0, 0 });
            numberOfSeatsNumericUpDown.Minimum = new decimal(new int[] { 1, 0, 0, 0 });
            numberOfSeatsNumericUpDown.Name = "numberOfSeatsNumericUpDown";
            numberOfSeatsNumericUpDown.Size = new Size(48, 23);
            numberOfSeatsNumericUpDown.TabIndex = 3;
            numberOfSeatsNumericUpDown.Value = new decimal(new int[] { 1, 0, 0, 0 });
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(432, 182);
            label1.Name = "label1";
            label1.Size = new Size(97, 15);
            label1.TabIndex = 4;
            label1.Text = "Number of seats:";
            // 
            // addReservationButton
            // 
            addReservationButton.Location = new Point(455, 238);
            addReservationButton.Name = "addReservationButton";
            addReservationButton.Size = new Size(104, 23);
            addReservationButton.TabIndex = 5;
            addReservationButton.Text = "Add Reservation";
            addReservationButton.UseVisualStyleBackColor = true;
            addReservationButton.Click += addReservationButton_Click;
            // 
            // tripLabel
            // 
            tripLabel.AutoSize = true;
            tripLabel.Location = new Point(394, 61);
            tripLabel.Name = "tripLabel";
            tripLabel.Size = new Size(38, 15);
            tripLabel.TabIndex = 6;
            tripLabel.Text = "label2";
            // 
            // TripForm
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(731, 422);
            Controls.Add(tripLabel);
            Controls.Add(addReservationButton);
            Controls.Add(label1);
            Controls.Add(numberOfSeatsNumericUpDown);
            Controls.Add(clientNameTextBox);
            Controls.Add(seatsDataGridView);
            Controls.Add(cancelReservationButton);
            Name = "TripForm";
            Text = "TripForm";
            Load += TripForm_Load;
            ((System.ComponentModel.ISupportInitialize)seatsDataGridView).EndInit();
            ((System.ComponentModel.ISupportInitialize)numberOfSeatsNumericUpDown).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Button cancelReservationButton;
        private DataGridView seatsDataGridView;
        private DataGridViewTextBoxColumn seatNumberColumn;
        private DataGridViewTextBoxColumn seatClientNameColumn;
        private TextBox clientNameTextBox;
        private NumericUpDown numberOfSeatsNumericUpDown;
        private Label label1;
        private Button addReservationButton;
        private Label tripLabel;
    }
}