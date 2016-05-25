Chained Function

function [  accuracy_1, ...                     %creates a function           
            sensitivity_1, ...
            specificity_1, ...
            trainPerformance_1, ...             what the function will output
            testPerformance_1, ...
            accuracy_2, ...
            sensitivity_2, ...
            specificity_2, ...
            trainPerformance_2, ...
            testPerformance_2, ...
            accuracy_3, ...
            sensitivity_3, ...
            specificity_3, ...
            trainPerformance_3, ...
            testPerformance_3 ]       = chained_ANN_3stage(features, full_features, train_function, results) %required inputs
        
// Creates a function for repeated use of the same code with different features sets and algorithms
        


accuracy_1 = zeros(10, 10) %sets the variable for what the accuracy, sensitivity, specificity, training accuracy, and testing accuracy will be
sensitivity_1 = zeros(10, 10)
specificity_1 = zeros(10, 10)

trainPerformance_1 = zeros(10,10)
testPerformance_1 = zeros(10,10)

accuracy_2 = zeros(10, 10)
sensitivity_2 = zeros(10, 10)
specificity_2 = zeros(10, 10)

trainPerformance_2 = zeros(10,10)
testPerformance_2 = zeros(10,10)


accuracy_3 = zeros(10, 10)
sensitivity_3 = zeros(10, 10)
specificity_3 = zeros(10, 10)

trainPerformance_3 = zeros(10,10)
testPerformance_3 = zeros(10,10)



for j = 1:10 %varies the number of hidden neurons from 6 to 24
    
    x = (j*2) + 4
    
    net_00 = patternnet(x, train_function);%creates three neural networks with these hidden neurons
    net_11 = patternnet(x, train_function);
    net_22 = patternnet(x, train_function);

for y = 1:10

% first stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[pn_00,ps1_00] = mapstd(features);%inputs the features into the neural network
[ptrans_00,ps2_00] = processpca(pn_00,0.02); %uses principal component analysis to convert these possibly connected features to uncorrelated features

[netTrain_00, tr_00]  = train(net_00,ptrans_00,results);%trains the neural network
outp_00 = netTrain_00(ptrans_00);%stores the outputs

[c,cm,ind,per] = confusion(results, outp_00);%gets the results from the confusion matrix

accuracy_1(j, y) = (cm(1,1) + cm(2,2)) / sum(sum(cm));%calculates the accuracy, sensitivity, and specificity
sensitivity_1(j,y) = cm(2,2) / (cm(2,1) + cm(2,2));
specificity_1(j,y) = cm(1,1) / (cm(1,1) + cm(1,2));

trainTargets = results .* tr_00.trainMask{1};%computes for training    
testTargets = results  .* tr_00.testMask{1};`%computes for testing

trainPerformance_1(j, y) = 1 - perform(netTrain_00,trainTargets,outp_00);      
testPerformance_1(j,y) = 1 - perform(netTrain_00,testTargets,outp_00); 

% second stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

features_new = vertcat(full_features, outp_00);%combines the features and the output from the previous stage

[pn_11,ps1_11] = mapstd(features_new);
[ptrans_11,ps2_11] = processpca(pn_11,0.02);

[netTrain_11, tr_11]  = train(net_11,ptrans_11,results);
outp_11 = netTrain_11(ptrans_11);


[c,cm,ind,per] = confusion(results, outp_11);

accuracy_2(j, y) = (cm(1,1) + cm(2,2)) / sum(sum(cm));
sensitivity_2(j,y) = cm(2,2) / (cm(2,1) + cm(2,2));
specificity_2(j,y) = cm(1,1) / (cm(1,1) + cm(1,2));

trainTargets = results .* tr_11.trainMask{1};    
testTargets = results  .* tr_11.testMask{1};  

trainPerformance_2(j, y) = 1 - perform(netTrain_11,trainTargets,outp_11);      
testPerformance_2(j,y) = 1 - perform(netTrain_11,testTargets,outp_11); 




% third stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



features_new = vertcat(full_features, outp_11);%combines the features and the output from the previous stage

[pn_22,ps1_22] = mapstd(features_new);
[ptrans_22,ps2_22] = processpca(pn_22,0.02);

[netTrain_22, tr_22]  = train(net_22,ptrans_22,results);
outp_22 = netTrain_22(ptrans_22);


[c,cm,ind,per] = confusion(results, outp_22);

accuracy_3(j, y) = (cm(1,1) + cm(2,2)) / sum(sum(cm));
sensitivity_3(j,y) = cm(2,2) / (cm(2,1) + cm(2,2));
specificity_3(j,y) = cm(1,1) / (cm(1,1) + cm(1,2));

trainTargets = results .* tr_22.trainMask{1};    
testTargets = results  .* tr_22.testMask{1};  

trainPerformance_3(j, y) = 1 - perform(netTrain_22,trainTargets,outp_22);      
testPerformance_3(j,y) = 1 - perform(netTrain_22,testTargets,outp_22);

end
end

end























Hierarchical Function

function [  accuracy, ...
            sensitivity, ...    %outputs
            specificity, ...
            trainPerformance, ...
            testPerformance ]       = HeirarchicalScript(image_features, features, train_function, results)%inputs
        



%sets variables
accuracy = zeros(10, 10)
sensitivity = zeros(10, 10)
specificity = zeros(10, 10)

trainPerformance = zeros(10,10)
testPerformance = zeros(10,10)

for j = 1:10    %varies the number of hidden neurons from 6-24
    
    x = (j*2) + 4
    
    net_00 = patternnet(x, train_function);
    net_11 = patternnet(x, train_function);
    net_22 = patternnet(x, train_function);
    
for y = 1:10    %over 10 trials

% first stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[pn_00,ps1_00] = mapstd(image_features);%trains with only image features
[ptrans_00,ps2_00] = processpca(pn_00,0.02);

[netTrain_00, tr_00]  = train(net_00,ptrans_00,results);
outp_00 = netTrain_00(ptrans_00);

% second stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



[pn_11,ps1_11] = mapstd(features);%trains with only database features
[ptrans_11,ps2_11] = processpca(pn_11,0.02);

[netTrain_11, tr_11]  = train(net_11,ptrans_11,results);
outp_11 = netTrain_11(ptrans_11);



% third stage %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
features_new = vertcat(features, image_features, outp_11, outp_00);% combines the two outputs with the full feature set



[pn_22,ps1_22] = mapstd(features_new);
[ptrans_22,ps2_22] = processpca(pn_22,0.02);

[netTrain_22, tr_22]  = train(net_22,ptrans_22,results);
outp_22 = netTrain_22(ptrans_22);


[c,cm,ind,per] = confusion(results, outp_22);

accuracy(j, y) = (cm(1,1) + cm(2,2)) / sum(sum(cm));
sensitivity(j,y) = cm(2,2) / (cm(2,1) + cm(2,2));
specificity(j,y) = cm(1,1) / (cm(1,1) + cm(1,2));

trainTargets = results .* tr_22.trainMask{1};    
testTargets = results  .* tr_22.testMask{1};  

trainPerformance(j, y) = 1 - perform(netTrain_22,trainTargets,outp_22);      
testPerformance(j,y) = 1 - perform(netTrain_22,testTargets,outp_22);

end
end
end










Script

data = csvread('oasis_60.csv');%read the data from the csv file
features = data(:, 1:6)';%sets the first 6 columns as features
results = data(:,7)';%sets the last column as the output

train_function = 'trainbr';%sets the training algorithm to input into the function

image_features = csvread('image_features.csv');%reads the extracted image features
image_features = image_features';
full_features = vertcat(features, image_features);%combines the image features with the database features

features_no_mmse = horzcat(data(:, 1:2), data(:, 4:6));%tests without the mmse or image features
features_no_mmse = features_no_mmse';
full_features_no_mmse = vertcat(features_no_mmse, image_features);%tests without the mmse

  [accuracy_1, ...                      %calls the function made in chained_ANN_3stage code
            sensitivity_1, ...
            specificity_1, ...
            trainPerformance_1, ...
            testPerformance_1, ...
            accuracy_2, ...
            sensitivity_2, ...
            specificity_2, ...
            trainPerformance_2, ...
            testPerformance_2, ...
            accuracy_3, ...
            sensitivity_3, ...
            specificity_3, ...
            trainPerformance_3, ...
            testPerformance_3 ]      = chained_ANN_3stage(features, features, train_function, results) %inputs features & algorithms
      %tests without image features
        

%the no-image accuracies

csvwrite('noimage_accuracy_1.csv',accuracy_1);%writes the accuracies, sensitivities, and specificities to a csv file
csvwrite('noimage_sensitivity_1.csv',sensitivity_1);
csvwrite('noimage_specificity_1.csv',specificity_1);
csvwrite('noimage_trainPerf_1.csv',trainPerformance_1);
csvwrite('noimage_testPerf_1.csv',testPerformance_1);

csvwrite('noimage_accuracy_2.csv',accuracy_2);
csvwrite('noimage_sensitivity_2.csv',sensitivity_2);
csvwrite('noimage_specificity_2.csv',specificity_2);
csvwrite('noimage_trainPerf_2.csv',trainPerformance_2);
csvwrite('noimage_testPerf_2.csv',testPerformance_2);

csvwrite('noimage_accuracy_3.csv',accuracy_3);
csvwrite('noimage_sensitivity_3.csv',sensitivity_3);
csvwrite('noimage_specificity_3.csv',specificity_3);
csvwrite('noimage_trainPerf_3.csv',trainPerformance_3);
csvwrite('noimage_testPerf_3.csv',testPerformance_3);



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% with image

 [accuracy_1, ...
            sensitivity_1, ...
            specificity_1, ...
            trainPerformance_1, ...
            testPerformance_1, ...
            accuracy_2, ...
            sensitivity_2, ...
            specificity_2, ...
            trainPerformance_2, ...
            testPerformance_2, ...
            accuracy_3, ...
            sensitivity_3, ...
            specificity_3, ...
            trainPerformance_3, ...
            testPerformance_3 ]      = chained_ANN_3stage(features, full_features, train_function, results)
        %tests with image features

% write the image accuracies
%single stage stage network
csvwrite('accuracy_1.csv',accuracy_1);
csvwrite('sensitivity_1.csv',sensitivity_1);
csvwrite('specificity_1.csv',specificity_1);
csvwrite('trainPerf_1.csv',trainPerformance_1);
csvwrite('testPerf_1.csv',testPerformance_1);
%double stage network
csvwrite('accuracy_2.csv',accuracy_2);
csvwrite('sensitivity_2.csv',sensitivity_2);
csvwrite('specificity_2.csv',specificity_2);
csvwrite('trainPerf_2.csv',trainPerformance_2);
csvwrite('testPerf_2.csv',testPerformance_2);
%triple stage network
csvwrite('accuracy_3.csv',accuracy_3);
csvwrite('sensitivity_3.csv',sensitivity_3);
csvwrite('specificity_3.csv',specificity_3);
csvwrite('trainPerf_3.csv',trainPerformance_3);
csvwrite('testPerf_3.csv',testPerformance_3);

 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% heirarchical
  
        [  accuracy, ...
            sensitivity, ...
            specificity, ...
            trainPerformance, ...
            testPerformance ]       = HeirarchicalScript(features, full_features, train_function,results)
        %calls the function from the HeirarchicalScript code
        
        % heirarchical accuracies
        
csvwrite('hier_accuracy_3.csv',accuracy);
csvwrite('hier_sensitivity_3.csv',sensitivity);
csvwrite('hier_specificity_3.csv',specificity);
csvwrite('hier_trainPerf_3.csv',trainPerformance);
csvwrite('hier_testPerf_3.csv',testPerformance);

data = csvread('oasis_60.csv');%read the data from the csv file 
features = data(:, 1:6)';%sets the first 6 columns as features
results = data(:,7)';%sets the last column as the output

train_function = 'trainbr';%sets the training algorithm to input into the function

image_features = csvread('image_features.csv');%reads the extracted image features
image_features = image_features';
full_features = vertcat(features, image_features);%combines the image features with the database features

features_no_mmse = horzcat(data(:, 1:2), data(:, 4:6));%tests without the mmse or image features
features_no_mmse = features_no_mmse';
full_features_no_mmse = vertcat(features_no_mmse, image_features);%tests without the mmse

  [accuracy_1, ...                      %calls the function made in chained_ANN_3stage code
            sensitivity_1, ...
            specificity_1, ...
            trainPerformance_1, ...
            testPerformance_1, ...
            accuracy_2, ...
            sensitivity_2, ...
            specificity_2, ...
            trainPerformance_2, ...
            testPerformance_2, ...
            accuracy_3, ...
            sensitivity_3, ...
            specificity_3, ...
            trainPerformance_3, ...
            testPerformance_3 ]      = chained_ANN_3stage(features, features, train_function, results) %inputs features & algorithms
      %tests without image features
        

%the no-image accuracies

csvwrite('noimage_accuracy_1.csv',accuracy_1);%writes the accuracies, sensitivities, and specificities to a csv file
csvwrite('noimage_sensitivity_1.csv',sensitivity_1);
csvwrite('noimage_specificity_1.csv',specificity_1);
csvwrite('noimage_trainPerf_1.csv',trainPerformance_1);
csvwrite('noimage_testPerf_1.csv',testPerformance_1);

csvwrite('noimage_accuracy_2.csv',accuracy_2);
csvwrite('noimage_sensitivity_2.csv',sensitivity_2);
csvwrite('noimage_specificity_2.csv',specificity_2);
csvwrite('noimage_trainPerf_2.csv',trainPerformance_2);
csvwrite('noimage_testPerf_2.csv',testPerformance_2);

csvwrite('noimage_accuracy_3.csv',accuracy_3);
csvwrite('noimage_sensitivity_3.csv',sensitivity_3);
csvwrite('noimage_specificity_3.csv',specificity_3);
csvwrite('noimage_trainPerf_3.csv',trainPerformance_3);
csvwrite('noimage_testPerf_3.csv',testPerformance_3);



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% with image

 [accuracy_1, ...
            sensitivity_1, ...
            specificity_1, ...
            trainPerformance_1, ...
            testPerformance_1, ...
            accuracy_2, ...
            sensitivity_2, ...
            specificity_2, ...
            trainPerformance_2, ...
            testPerformance_2, ...
            accuracy_3, ...
            sensitivity_3, ...
            specificity_3, ...
            trainPerformance_3, ...
            testPerformance_3 ]      = chained_ANN_3stage(features, full_features, train_function, results)
        %tests with image features

% write the image accuracies
%single stage stage network
csvwrite('accuracy_1.csv',accuracy_1);
csvwrite('sensitivity_1.csv',sensitivity_1);
csvwrite('specificity_1.csv',specificity_1);
csvwrite('trainPerf_1.csv',trainPerformance_1);
csvwrite('testPerf_1.csv',testPerformance_1);
%double stage network
csvwrite('accuracy_2.csv',accuracy_2);
csvwrite('sensitivity_2.csv',sensitivity_2);
csvwrite('specificity_2.csv',specificity_2);
csvwrite('trainPerf_2.csv',trainPerformance_2);
csvwrite('testPerf_2.csv',testPerformance_2);
%triple stage network
csvwrite('accuracy_3.csv',accuracy_3);
csvwrite('sensitivity_3.csv',sensitivity_3);
csvwrite('specificity_3.csv',specificity_3);
csvwrite('trainPerf_3.csv',trainPerformance_3);
csvwrite('testPerf_3.csv',testPerformance_3);

 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% hierarchical
  
        [  accuracy, ...
            sensitivity, ...
            specificity, ...
            trainPerformance, ...
            testPerformance ]       = HeirarchicalScript(features, full_features, train_function,results)
        %calls the function from the HeirarchicalScript code
        
        % heirarchical accuracies
        
csvwrite('hier_accuracy_3.csv',accuracy);
csvwrite('hier_sensitivity_3.csv',sensitivity);
csvwrite('hier_specificity_3.csv',specificity);
csvwrite('hier_trainPerf_3.csv',trainPerformance);
csvwrite('hier_testPerf_3.csv',testPerformance);


