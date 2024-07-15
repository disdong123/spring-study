(async () => {
    const requests = [];

    for (let i = 0; i < 10; i++) {
        const request = fetch('http://localhost:8080/restclient/webclient')
            .then(data => {
                console.log(`요청 ${i+1} 성공:`);
                return data;
            })
            .catch(error => {
                console.error(`요청 ${i+1} 실패:`, error);
                throw error;
            });

        requests.push(request);
    }
})()