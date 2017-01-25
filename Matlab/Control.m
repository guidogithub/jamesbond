clc, clearvars;

global keys;

%% Remember what keys are pressed
keys = zeros(5,1);

%% Open figure (to capture keyboard events)
set(figure(1),'KeyPressFcn',@(~,event) pressed(event.Key),...
    'KeyReleaseFcn',@(~,event) released(event.Key));

%% Update pressed keys
function pressed(key)
global keys;
switch key
    case 'uparrow'
        keys(1) = 1;
        keys(2) = 0;
    case 'downarrow'
        keys(1) = 0;
        keys(2) = 1;
    case 'leftarrow'
        keys(3) = 1;
        keys(4) = 0;
    case 'rightarrow'
        keys(3) = 0;
        keys(4) = 1;
    case 'escape'
        keys(5) = 1;
end
drive;
end

%% Update released keys
function released(key)
global keys;
switch key
    case 'uparrow'
        keys(1) = 0;
    case 'downarrow'
        keys(2) = 0;
    case 'leftarrow'
        keys(3) = 0;
    case 'rightarrow'
        keys(4) = 0;
end
drive;
end

%% Communicate with Pi
function drive
global keys;
persistent u;

% Connect
if isempty(u)
    u = udp('192.168.0.197',8010,'LocalPort',9091);
    u.Timeout = 10;
    u.OutputBufferSize = 20000;
    fopen(u);
end

% Disconnect
if keys(5)
    fclose(u);
    clear u;
    close;
    return;
end

forward = 50*(keys(1)-keys(2));
rotate  = 50*(keys(3)-keys(4));

% Send message
fwrite(u,sprintf('1f%da%dr%db%dt',abs(forward),forward>=0,...
    abs(rotate),rotate*forward>=0));

tmp = [-rotate; forward];
len = norm(tmp);
arr = tmp/len;

figure(1);
plot(0,0,'k.','MarkerSize',50);
hold on;
quiver(0,0,arr(1),arr(2),0,'k','LineWidth',5,'MaxHeadSize',len/50);
axis([-1 1 -1 1]);
axis square;
axis off;
hold off;

end
