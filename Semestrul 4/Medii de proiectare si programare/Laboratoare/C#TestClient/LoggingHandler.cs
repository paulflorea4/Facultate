using System;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace CSharpTestClient
{
    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler() { }
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine($"Request: {request.Method} {request.RequestUri}");

            var response = await base.SendAsync(request, cancellationToken);

            Console.WriteLine($"Response: {(int) response.StatusCode}");
            return response;
        }
    }
}
