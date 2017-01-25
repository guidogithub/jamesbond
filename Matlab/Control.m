clc, clearvars;

%% Connect to server
u                  = udp('192.168.0.197',8010,'LocalPort',9091);
u.Timeout          = 10; % Maximum time-out in seconds
u.OutputBufferSize = 20000;
fopen(u);

%% Reference in mm/mrad
xstr     = 50;
ystr     = 50;
psistr   = 0;

signxs   = num2str(1);
signys   = num2str(1);
signpsis = num2str(1);

if sign(xstr)   == -1, signxs   = num2str(0); end
if sign(ystr)   == -1, signys   = num2str(0); end
if sign(psistr) == -1, signpsis = num2str(0); end

xstr   = num2str(xstr);
ystr   = num2str(ystr);
psistr = num2str(psistr);

% Match with Pi
data = strcat(num2str(1),'x',xstr,'a',signxs,'y',ystr,'b',signys,...
    'p',psistr,'c',signpsis,'t');

fwrite(u,data);

%% Disconnect from server
fclose(u);
