namespace FirmaTransportC_.Model
{
    public abstract class Entity<TId>
    {
        private TId id;

        public Entity(TId id)
        {
            this.id = id;
        }

        public TId Id
        {
            get { return id; }
            set { id = value; }
        }
    }
}